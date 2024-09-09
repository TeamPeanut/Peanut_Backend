package com.springboot.peanut.service;

import com.springboot.peanut.dto.food.FoodDetailInfoDto;
import com.springboot.peanut.dto.signDto.ResultDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface FoodDetailService {
    List<FoodDetailInfoDto> getFoodDetailInfo(List<String> name , HttpServletRequest request);
    ResultDto createMealInfo(String mealTime,HttpServletRequest request);
}
