package com.springboot.peanut.repository.FoodNutrition;

import com.springboot.peanut.entity.FoodNutrition;

import java.util.List;

public interface FoodNutritionRepositoryCustom {
    List<FoodNutrition> findFoodNutritionByFoodName(List<String> foodName);
}
