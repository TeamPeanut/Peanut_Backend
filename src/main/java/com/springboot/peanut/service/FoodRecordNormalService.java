package com.springboot.peanut.service;

import com.springboot.peanut.dto.food.FoodNutritionDto;
import com.springboot.peanut.dto.signDto.ResultDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface FoodRecordNormalService {
    List<FoodNutritionDto> getFoodNutritionByName(List<String> name, HttpServletRequest request);
    ResultDto saveNormalMealInfo(String mealTime, int servingCount, HttpServletRequest request);
}
