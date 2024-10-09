package com.springboot.peanut.data.dao;

import com.springboot.peanut.data.dto.food.FoodNameNutrionDto;
import com.springboot.peanut.data.entity.MealInfo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface MealDao {
    void save(MealInfo mealInfo);
    List<FoodNameNutrionDto> getMealInfoByEatTime(LocalDate date, Long userId, String eatTime);

}
