package com.springboot.peanut.data.repository.FoodNutrition;

import com.springboot.peanut.data.entity.FoodNutrition;

import java.util.List;

public interface FoodNutritionRepositoryCustom {
    List<FoodNutrition> findFoodNutritionByFoodName(List<String> foodName);
    List<FoodNutrition> findFoodNutritionByFoodNameKor(List<String> foodName);
}
