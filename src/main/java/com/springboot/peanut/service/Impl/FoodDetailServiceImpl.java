package com.springboot.peanut.service.Impl;

import com.springboot.peanut.dao.MealDao;
import com.springboot.peanut.dto.CommonResponse;
import com.springboot.peanut.dto.food.FoodDetailInfoDto;
import com.springboot.peanut.dto.food.MealResponseDto;
import com.springboot.peanut.dto.signDto.ResultDto;
import com.springboot.peanut.entity.*;
import com.springboot.peanut.jwt.JwtProvider;
import com.springboot.peanut.repository.BloodSugar.BloodSugarRepository;
import com.springboot.peanut.repository.FoodNutrition.FoodNutritionRepository;
import com.springboot.peanut.repository.UserRepository;
import com.springboot.peanut.service.FoodDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FoodDetailServiceImpl implements FoodDetailService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final FoodNutritionRepository foodNutritionRepository;
    private final BloodSugarRepository bloodSugarRepository;
    private final MealDao mealDao;

    @Override
    public List<FoodDetailInfoDto> getFoodDetailInfo(List<String> name, HttpServletRequest request) {
        User user = authenticationToken(request);

        List<FoodNutrition> foodNutritionList = foodNutritionRepository.findFoodNutritionByFoodName(name);
        log.info("[foodNutritionList] : {} ", foodNutritionList );
        double expectedBloodSugar = calculateExpectedBloodSugar(user.getId(),foodNutritionList);
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
    public ResultDto createMealInfo(String mealTime, HttpServletRequest request) {
        User user = authenticationToken(request);
        List<FoodDetailInfoDto> foodDetailInfoDtoList = (List<FoodDetailInfoDto>)request.getSession().getAttribute("foodDetailInfoDtoList");
        double expectedBloodSugar = (double)request.getSession().getAttribute("expectedBloodSugar");
        if (user == null) {
            ResultDto resultDto = new ResultDto();
            resultDto.setDetailMessage("사용자 인증에 실패했습니다.");
            setFail(resultDto);  // 실패 응답 설정
            return resultDto;
        }

        // 음식 영양성분 아이디만 가져오기
        List<Long> foodNutritionIds = foodDetailInfoDtoList.stream()
                .map(FoodDetailInfoDto::getFoodId)
                .collect(Collectors.toList());

        // id로 영양성분 데이터 가져오기
        List<FoodNutrition> foodNutritionList = foodNutritionRepository.findAllById(foodNutritionIds);

        MealInfo mealInfo = MealInfo.MealInfo(mealTime,expectedBloodSugar,foodNutritionList,user);

        mealDao.save(mealInfo);

        // MealInfo 객체 생성
        ResultDto resultDto = new ResultDto();
        resultDto.setDetailMessage("식사 기록 저장 완료!");
        setSuccess(resultDto);

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

    public User authenticationToken(HttpServletRequest request) {
        String token = jwtProvider.resolveToken(request);
        String email = jwtProvider.getUsername(token);
        User user = userRepository.findByEmail(email);
        return user;
    }

    private void setSuccess(ResultDto resultDto) {
        resultDto.setSuccess(true);
        resultDto.setCode(CommonResponse.SUCCESS.getCode());
        resultDto.setMsg(CommonResponse.SUCCESS.getMsg());

    }

    private void setFail(ResultDto resultDto) {
        resultDto.setSuccess(false);
        resultDto.setCode(CommonResponse.Fail.getCode());
        resultDto.setMsg(CommonResponse.Fail.getMsg());
    }
}
