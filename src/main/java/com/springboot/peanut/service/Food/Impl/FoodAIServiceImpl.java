package com.springboot.peanut.service.Food.Impl;

import com.springboot.peanut.S3.S3Uploader;
import com.springboot.peanut.data.dao.FoodPredictDao;
import com.springboot.peanut.data.dao.MealDao;
import com.springboot.peanut.data.dto.food.FoodDetailInfoDto;
import com.springboot.peanut.data.dto.food.FoodPredictDto;
import com.springboot.peanut.data.dto.food.FoodPredictResponseDto;
import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.data.entity.*;
import com.springboot.peanut.data.repository.BloodSugar.BloodSugarRepository;
import com.springboot.peanut.data.repository.FoodNutrition.FoodNutritionRepository;
import com.springboot.peanut.service.Food.FoodAIService;
import com.springboot.peanut.jwt.JwtAuthenticationService;
import com.springboot.peanut.service.Result.ResultStatusService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FoodAIServiceImpl implements FoodAIService {

    private final FoodPredictDao foodPredictDao;
    private final S3Uploader s3Uploader;
    private final WebClient webClient;
    private final Logger logger = LoggerFactory.getLogger(FoodAIService.class);
    private final FoodNutritionRepository foodNutritionRepository;
    private final BloodSugarRepository bloodSugarRepository;
    private final MealDao mealDao;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final ResultStatusService resultStatusService;

    public FoodAIServiceImpl(FoodPredictDao foodPredictDao, S3Uploader s3Uploader, WebClient.Builder webClientBuilder,FoodNutritionRepository foodNutritionRepository,
                             BloodSugarRepository bloodSugarRepository,MealDao mealDao,JwtAuthenticationService jwtAuthenticationService,ResultStatusService resultStatusService  ) throws IOException {
        this.foodPredictDao = foodPredictDao;
        this.s3Uploader = s3Uploader;
        this.webClient = webClientBuilder.baseUrl("http://52.78.145.50:8000").build();
        this.foodNutritionRepository = foodNutritionRepository;
        this.bloodSugarRepository = bloodSugarRepository;
        this.mealDao = mealDao;
        this.jwtAuthenticationService = jwtAuthenticationService;
        this.resultStatusService = resultStatusService;
    }

    @Override
    public FoodPredictResponseDto FoodNamePredict(MultipartFile foodImage,HttpServletRequest request) {
        FoodPredictResponseDto foodPredictResponseDto = null;
        try {
            String imageUrl = s3Uploader.uploadImage(foodImage, "peanut/before");
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("image_url", imageUrl);

            Mono<FoodPredictResponseDto> response = webClient.post()
                    .uri("/predict")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(FoodPredictResponseDto.class);

            foodPredictResponseDto = response.block();

            logger.info("Received Response: {}", foodPredictResponseDto);

            if (foodPredictResponseDto != null) {
                FoodPredict foodPredict = new FoodPredict();
                foodPredict.setFoodName(foodPredictResponseDto.getPredictions().stream().map(FoodPredictDto::getFoodName).collect(Collectors.toList()).toString());
                foodPredict.setAccuracy(foodPredictResponseDto.getPredictions().stream().map(p -> String.valueOf(p.getAccuracy())).collect(Collectors.toList()).toString());
                foodPredict.setImageUrl(foodPredictResponseDto.getImage_url());
                foodPredictDao.saveFoodPredictResults(foodPredict);

                logger.info("Saved FoodPredict Result: {}", foodPredict);

            }
            request.getSession().setAttribute("imageUrl", foodPredictResponseDto.getImage_url());
        } catch (WebClientException | IOException e) {
            logger.error("Error occurred while fetching results: ", e);
            e.printStackTrace();
        }

        return foodPredictResponseDto;
    }

    @Override
    public List<FoodDetailInfoDto> getFoodDetailInfo(List<String> name, HttpServletRequest request) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);

        List<FoodNutrition> foodNutritionList = foodNutritionRepository.findFoodNutritionByFoodName(name);
        log.info("[foodNutritionList] : {} ", foodNutritionList );
        double expectedBloodSugar = calculateExpectedBloodSugar(user.get().getId(),foodNutritionList);
        List<FoodDetailInfoDto> foodDetailInfoDtoList = foodNutritionList.stream().map(foodNutrition ->
                new FoodDetailInfoDto(
                        foodNutrition.getId(),
                        foodNutrition.getName(),
                        foodNutrition.getCarbohydrate(),
                        foodNutrition.getProtein(),
                        foodNutrition.getFat(),
                        foodNutrition.getCholesterol(),
                        foodNutrition.getGlIndex(),
                        foodNutrition.getGiIndex(),
                        expectedBloodSugar
                )).collect(Collectors.toList());

        request.getSession().setAttribute("foodDetailInfoDtoList", foodDetailInfoDtoList);
        request.getSession().setAttribute("expectedBloodSugar", expectedBloodSugar);
        return foodDetailInfoDtoList;
    }

    @Override
    public ResultDto createAIMealInfo(String mealTime, HttpServletRequest request) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);
        List<FoodDetailInfoDto> foodDetailInfoDtoList = (List<FoodDetailInfoDto>)request.getSession().getAttribute("foodDetailInfoDtoList");
        double expectedBloodSugar = (double)request.getSession().getAttribute("expectedBloodSugar");

        String imageUrl = (String)request.getSession().getAttribute("imageUrl");
        if (user == null) {
            ResultDto resultDto = new ResultDto();
            resultDto.setDetailMessage("사용자 인증에 실패했습니다.");
            resultStatusService.setFail(resultDto);  // 실패 응답 설정
            return resultDto;
        }

        // 음식 영양성분 아이디만 가져오기
        List<Long> foodNutritionIds = foodDetailInfoDtoList.stream()
                .map(FoodDetailInfoDto::getFoodId)
                .collect(Collectors.toList());

        // id로 영양성분 데이터 가져오기
        List<FoodNutrition> foodNutritionList = foodNutritionRepository.findAllById(foodNutritionIds);

        MealInfo mealInfo = MealInfo.createMeal(mealTime,imageUrl,expectedBloodSugar,foodNutritionList,user.get());

        mealDao.save(mealInfo);

        // MealInfo 객체 생성
        ResultDto resultDto = new ResultDto();
        resultDto.setDetailMessage("식사 기록 저장 완료!");
        resultStatusService.setSuccess(resultDto);

        return resultDto;
    }

    public double calculateExpectedBloodSugar(Long userId, List<FoodNutrition> foodNutritionList) {
        // 사용자 아이디로 최근 혈당 기록 가져오기
        Optional<BloodSugar> currentBloodSugar = bloodSugarRepository.findClosestBloodSugar(userId);
        log.info("[currentBloodSugar] : {} ", currentBloodSugar );
        if (!currentBloodSugar.isPresent()) {
            throw new RuntimeException("최근 등록된 혈당 기록이 없습니다.");
        }

        // 현재 혈당 값을 가져옴
        double currentBloodSugarinfo = Double.parseDouble(currentBloodSugar.get().getBloodSugarLevel());
        double totalBloodSugarIncrease = 0.0;

        // 음식의 GI, GL, 탄수화물 정보를 기반으로 혈당 상승량 계산
        for (FoodNutrition food : foodNutritionList) {
            // GL 계산
            double gl = (food.getGiIndex() * food.getCarbohydrate()) / 100.0;

            // 예상 혈당 상승량 계산 (GL * 1.8)
            double bloodSugarIncrease = gl * 1.8;

            // 총 혈당 상승량에 더하기
            totalBloodSugarIncrease += bloodSugarIncrease;
        }
        // 평균 혈당 상승량 계산
        double averageBloodSugarIncrease = totalBloodSugarIncrease / foodNutritionList.size();

        // 최종 예상 혈당 계산 (현재 혈당 + 평균 혈당 상승량)
        double expectedBloodSugar = currentBloodSugarinfo + averageBloodSugarIncrease;

        return expectedBloodSugar;
    }



}

