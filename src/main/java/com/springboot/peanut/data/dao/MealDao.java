package com.springboot.peanut.data.dao;

import com.springboot.peanut.data.dto.food.FoodNameNutrionDto;
import com.springboot.peanut.data.entity.MealInfo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MealDao {
    void save(MealInfo mealInfo);
    Optional<List<MealInfo>> getByUserAllMealInfo(LocalDate date,Long userId);
    Optional<MealInfo> getMealInfoByEatTime(LocalDate date, Long userId, String eatTime);

}
