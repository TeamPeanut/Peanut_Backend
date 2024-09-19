package com.springboot.peanut.repository.FoodNutrition;

import com.springboot.peanut.entity.FoodNutrition;

import java.util.List;
import java.util.Optional;

public interface FoodNutritionRepositoryCustom {
    List<FoodNutrition> findFoodNutritionByFoodName(List<String> foodName);
    List<FoodNutrition> findFoodNutritionByFoodNameKor(List<String> foodName);
}
