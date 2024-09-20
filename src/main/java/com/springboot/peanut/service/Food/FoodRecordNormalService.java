package com.springboot.peanut.service.Food;

import com.springboot.peanut.data.dto.food.FoodNutritionDto;
import com.springboot.peanut.data.dto.signDto.ResultDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface FoodRecordNormalService {
    List<FoodNutritionDto> getFoodNutritionByName(String name, HttpServletRequest request);
    ResultDto saveNormalMealInfoImage(MultipartFile foodImage, HttpServletRequest request) throws IOException;
    ResultDto saveNormalMealInfo(String mealTime, int servingCount, HttpServletRequest request);
}
