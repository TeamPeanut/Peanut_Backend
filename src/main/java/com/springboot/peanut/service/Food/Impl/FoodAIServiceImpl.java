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
import java.util.*;
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
    public FoodPredictResponseDto FoodNamePredict(MultipartFile foodImage, HttpServletRequest request) {
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
                List<String> recognizedFoodNames = foodPredictResponseDto.getPredictions().stream()
                        .map(FoodPredictDto::getFoodName)
                        .collect(Collectors.toList());

                // 인식된 음식을 세션에 자동으로 저장
                addFoodsToSession(recognizedFoodNames, request);

                logger.info("Saved FoodPredict Result: {}", recognizedFoodNames);
            }

            request.getSession().setAttribute("imageUrl", foodPredictResponseDto.getImage_url());
        } catch (WebClientException | IOException e) {
            logger.error("Error occurred while fetching results: ", e);
            e.printStackTrace();
        }

        return foodPredictResponseDto;
    }

    @Override
    public List<FoodDetailInfoDto> getFoodDetailInfo(HttpServletRequest request) {
        List<FoodDetailInfoDto> foodDetailInfoDtoList = (List<FoodDetailInfoDto>) request.getSession().getAttribute("foodDetailInfoDtoList");
        if (foodDetailInfoDtoList == null) {
            return new ArrayList<>(); // 세션에 음식 정보가 없으면 빈 리스트 반환
        }
        return foodDetailInfoDtoList; // 세션에 저장된 모든 음식 정보 반환
    }

    // 사용자 직접 추가 음식 정보 저장
    @Override
    public ResultDto addCustomFood(String foodName, int servingCount, HttpServletRequest request) {
        List<FoodDetailInfoDto> foodDetailInfoDtoList = (List<FoodDetailInfoDto>) request.getSession().getAttribute("foodDetailInfoDtoList");
        ResultDto resultDto = new ResultDto();
        if (foodDetailInfoDtoList == null) {
            foodDetailInfoDtoList = new ArrayList<>();
        }

        Optional<FoodNutrition> foodNutritionOptional = foodNutritionRepository.findByName(foodName);
        if (foodNutritionOptional.isPresent()) {
            FoodNutrition foodNutrition = foodNutritionOptional.get();
            double expectedBloodSugar = calculateBloodSugarIncrease(foodNutrition, servingCount);
            foodDetailInfoDtoList.add(new FoodDetailInfoDto(
                    foodNutrition.getId(),
                    foodNutrition.getName(),
                    foodNutrition.getCarbohydrate(),
                    foodNutrition.getProtein(),
                    foodNutrition.getFat(),
                    foodNutrition.getCholesterol(),
                    foodNutrition.getGlIndex(),
                    foodNutrition.getGiIndex(),
                    expectedBloodSugar,
                    servingCount
            ));
            resultDto.setDetailMessage("음식 추가 완료");
            resultStatusService.setSuccess(resultDto);
        }
        request.getSession().setAttribute("foodDetailInfoDtoList", foodDetailInfoDtoList);
        return resultDto;
    }

    @Override
    public ResultDto createAIMealInfo(String mealTime, HttpServletRequest request) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);
        List<FoodDetailInfoDto> foodDetailInfoDtoList = (List<FoodDetailInfoDto>)request.getSession().getAttribute("foodDetailInfoDtoList");

        if (user.isEmpty() || foodDetailInfoDtoList == null) {
            ResultDto resultDto = new ResultDto();
            resultDto.setDetailMessage("사용자 인증에 실패했거나 음식 정보가 없습니다.");
            resultStatusService.setFail(resultDto);  // 실패 응답 설정
            return resultDto;
        }

        String imageUrl = (String)request.getSession().getAttribute("imageUrl");

        // 음식 영양성분 아이디만 가져오기
        List<Long> foodNutritionIds = foodDetailInfoDtoList.stream()
                .map(FoodDetailInfoDto::getFoodId)
                .collect(Collectors.toList());

        // id로 영양성분 데이터 가져오기
        List<FoodNutrition> foodNutritionList = foodNutritionRepository.findAllById(foodNutritionIds);

        MealInfo mealInfo = MealInfo.createMeal(mealTime, imageUrl, calculateTotalExpectedBloodSugar(user.get().getId(), foodNutritionList, foodDetailInfoDtoList), foodNutritionList, user.get());

        mealDao.save(mealInfo);

        // MealInfo 객체 생성
        ResultDto resultDto = new ResultDto();
        resultDto.setDetailMessage("식사 기록 저장 완료!");
        resultStatusService.setSuccess(resultDto);

        return resultDto;
    }
    // 인식된 음식을 세션에 저장하는 메서드
    private void addFoodsToSession(List<String> foodNames, HttpServletRequest request) {
        List<FoodDetailInfoDto> foodDetailInfoDtoList = (List<FoodDetailInfoDto>) request.getSession().getAttribute("foodDetailInfoDtoList");

        if (foodDetailInfoDtoList == null) {
            foodDetailInfoDtoList = new ArrayList<>();
        }

        // 음식 이름으로 영양 정보 조회 후 세션에 저장
        List<FoodNutrition> foodNutritionList = foodNutritionRepository.findFoodNutritionByFoodName(foodNames);
        for (FoodNutrition food : foodNutritionList) {
            double expectedBloodSugar = calculateBloodSugarIncrease(food, 1); // 기본 인분 수를 1로 지정
            foodDetailInfoDtoList.add(new FoodDetailInfoDto(
                    food.getId(),
                    food.getName(),
                    food.getCarbohydrate(),
                    food.getProtein(),
                    food.getFat(),
                    food.getCholesterol(),
                    food.getGlIndex(),
                    food.getGiIndex(),
                    expectedBloodSugar,
                    1 // 기본 인분 수를 1로 저장
            ));
        }

        request.getSession().setAttribute("foodDetailInfoDtoList", foodDetailInfoDtoList);
    }
    // 총 혈당 상승량 계산 메서드
    private double calculateTotalExpectedBloodSugar(Long userId, List<FoodNutrition> foodNutritionList, List<FoodDetailInfoDto> foodDetailInfoDtoList) {
        Optional<BloodSugar> currentBloodSugar = bloodSugarRepository.findClosestBloodSugar(userId);
        log.info("[currentBloodSugar] : {} ", currentBloodSugar );

        if (!currentBloodSugar.isPresent()) {
            throw new RuntimeException("최근 등록된 혈당 기록이 없습니다.");
        }

        double totalBloodSugarIncrease = 0.0;
        for (FoodDetailInfoDto foodDetail : foodDetailInfoDtoList) {
            Optional<FoodNutrition> food = foodNutritionRepository.findById(foodDetail.getFoodId());
            if (food.isPresent()) {
                totalBloodSugarIncrease += calculateBloodSugarIncrease(food.get(), foodDetail.getServingCount());
            }
        }

        return Double.parseDouble(currentBloodSugar.get().getBloodSugarLevel()) + totalBloodSugarIncrease;
    }

    // 개별 음식의 혈당 상승량 계산 메서드
    private double calculateBloodSugarIncrease(FoodNutrition food, int servingCount) {
        double gl = (food.getGiIndex() * food.getCarbohydrate()) / 100.0;
        return gl * 1.8 * servingCount; // GL * 1.8 * 서빙수
    }
}