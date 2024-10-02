package com.springboot.peanut.service.Food.Impl;

import com.springboot.peanut.S3.S3Uploader;
import com.springboot.peanut.data.dao.MealDao;
import com.springboot.peanut.data.dto.food.FoodNutritionDto;
import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.data.entity.BloodSugar;
import com.springboot.peanut.data.entity.FoodNutrition;
import com.springboot.peanut.data.entity.MealInfo;
import com.springboot.peanut.data.entity.User;
import com.springboot.peanut.data.repository.BloodSugar.BloodSugarRepository;
import com.springboot.peanut.data.repository.FoodNutrition.FoodNutritionRepository;
import com.springboot.peanut.service.Food.FoodRecordNormalService;
import com.springboot.peanut.service.Result.ResultStatusService;
import com.springboot.peanut.jwt.JwtAuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoodRecordNormalServiceImpl implements FoodRecordNormalService {

    private final FoodNutritionRepository foodNutritionRepository;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final BloodSugarRepository bloodSugarRepository;
    private final ResultStatusService resultStatusService;
    private final MealDao mealDao;
    private final S3Uploader s3Uploader;

    @Override
    public List<FoodNutritionDto> getFoodNutritionByName(List<String> name, HttpServletRequest request) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);
        log.info("[user] : {} " , user.get().getEmail());
        List<FoodNutrition> foodNutritionList = foodNutritionRepository.findFoodNutritionByFoodNameKor(name);
        log.info("[foodNutritionList] : {} ", foodNutritionList );
        List<FoodNutritionDto> foodDetailInfoDtoList = foodNutritionList.stream().map(foodNutrition ->
                new FoodNutritionDto(
                        foodNutrition.getId(),
                        foodNutrition.getName(),
                        foodNutrition.getCarbohydrate(),
                        foodNutrition.getProtein(),
                        foodNutrition.getFat(),
                        foodNutrition.getCholesterol(),
                        foodNutrition.getGlIndex(),
                        foodNutrition.getGiIndex()
                )).collect(Collectors.toList());

        request.getSession().setAttribute("foodDetailInfoDtoList", foodDetailInfoDtoList);
        return foodDetailInfoDtoList;

    }

    @Override
    public ResultDto saveNormalMealInfoImage(MultipartFile foodImage, HttpServletRequest request) throws IOException {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);
        String imageUrl = s3Uploader.uploadImage(foodImage,"peanut");
        ResultDto resultDto = new ResultDto();

        if(user == null){
            resultDto.setDetailMessage("존재하지 않는 회원 입니다.");
            resultStatusService.setFail(resultDto);
            return resultDto;
        }else{
            resultDto.setDetailMessage("이미지 등록 완료.");
            resultStatusService.setSuccess(resultDto);
            request.getSession().setAttribute("imageUrl", imageUrl);
            return resultDto;

        }
    }

    @Override
    public ResultDto saveNormalMealInfo(String mealTime, List<Integer> servingCounts, HttpServletRequest request) {
        Optional<User> userOpt = jwtAuthenticationService.authenticationToken(request);
        ResultDto resultDto = new ResultDto();

        if (!userOpt.isPresent()) {
            resultDto.setDetailMessage("사용자 인증에 실패했습니다.");
            resultStatusService.setFail(resultDto);
            return resultDto;
        }

        User user = userOpt.get();
        List<FoodNutritionDto> foodNutritionDtoList = (List<FoodNutritionDto>) request.getSession().getAttribute("foodDetailInfoDtoList");
        String imageUrl = (String) request.getSession().getAttribute("imageUrl");

        if (foodNutritionDtoList == null || foodNutritionDtoList.isEmpty()) {
            resultDto.setDetailMessage("음식 정보가 없습니다.");
            resultStatusService.setFail(resultDto);
            return resultDto;
        }

        if (servingCounts == null || servingCounts.isEmpty() || servingCounts.size() != foodNutritionDtoList.size()) {
            resultDto.setDetailMessage("인분 정보가 올바르지 않습니다.");
            resultStatusService.setFail(resultDto);
            return resultDto;
        }

        List<Long> foodNutritionIds = foodNutritionDtoList.stream()
                .map(FoodNutritionDto::getFoodId)
                .collect(Collectors.toList());

        List<FoodNutrition> foodNutritionList = foodNutritionRepository.findAllById(foodNutritionIds);

        if (foodNutritionList.isEmpty()) {
            resultDto.setDetailMessage("해당하는 음식 정보를 찾을 수 없습니다.");
            resultStatusService.setFail(resultDto);
            return resultDto;
        }

        // 예상 혈당 계산
        double expectedBloodSugar = calculateExpectedBloodSugar(user.getId(), servingCounts, foodNutritionList);

        // 식사 정보 생성 및 저장
        MealInfo mealInfo = MealInfo.createMeal(
                mealTime,
                imageUrl,
                expectedBloodSugar,
                foodNutritionList,
                user
        );
        mealDao.save(mealInfo);

        resultDto.setDetailMessage("식사 기록 저장 완료!");
        resultStatusService.setSuccess(resultDto);

        return resultDto;
    }
    private double calculateExpectedBloodSugar(Long userId, List<Integer> servingCounts, List<FoodNutrition> foodNutritionList) {
        // 사용자 아이디로 최근 혈당 기록 가져오기
        Optional<BloodSugar> currentBloodSugarOpt = bloodSugarRepository.findClosestBloodSugar(userId);

        if (!currentBloodSugarOpt.isPresent()) {
            throw new RuntimeException("최근 등록된 혈당 기록이 없습니다.");
        }

        double currentBloodSugar = Double.parseDouble(currentBloodSugarOpt.get().getBloodSugarLevel());
        double totalBloodSugarIncrease = 0.0;

        // 각 음식의 예상 혈당 상승량 계산
        for (int i = 0; i < foodNutritionList.size(); i++) {
            FoodNutrition food = foodNutritionList.get(i);
            int servingCount = servingCounts.get(i);

            // GL 계산 (GI * 탄수화물)
            double gl = (food.getGiIndex() * food.getCarbohydrate()) / 100.0;

            // 예상 혈당 상승량 계산 (GL * 1.8 * 인분 수)
            double bloodSugarIncrease = gl * 1.8 * servingCount;

            // 총 혈당 상승량에 더하기
            totalBloodSugarIncrease += bloodSugarIncrease;
        }

        // 최종 예상 혈당 계산 (현재 혈당 + 총 혈당 상승량)
        return currentBloodSugar + totalBloodSugarIncrease;
    }
}
