package com.springboot.peanut.data.dao.Impl;

import com.springboot.peanut.data.dao.MealDao;

import com.springboot.peanut.data.dto.food.FoodNameNutrionDto;
import com.springboot.peanut.data.entity.FoodNutrition;
import com.springboot.peanut.data.entity.MealInfo;
import com.springboot.peanut.data.repository.MealInfo.MealInfoRepository;
import com.springboot.peanut.data.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealDaoImpl implements MealDao {
    private final MealInfoRepository mealInfoRepository;


    @Override
    public void save(MealInfo mealInfo) {
        mealInfoRepository.save(mealInfo);
    }

    @Override
    public List<FoodNameNutrionDto> getMealInfoByEatTime(LocalDate date, Long userId , String eatTime) {
        Optional<List<MealInfo>> mealInfoList = mealInfoRepository.getMealInfoListByEatTime(date,userId,eatTime);
        for(MealInfo mealInfo : mealInfoList.get()){
            List<String> foodNames = mealInfo.getFoodNutritionList().stream()
                    .map(FoodNutrition::getName)
                    .collect(Collectors.toList());
        }

        return List.of();
    }
}
