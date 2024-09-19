package com.springboot.peanut.service.Food;

import com.springboot.peanut.dto.food.FoodDetailInfoDto;
import com.springboot.peanut.dto.signDto.ResultDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface FoodRecordAIService {
    List<FoodDetailInfoDto> getFoodDetailInfo(List<String> name , HttpServletRequest request);

    ResultDto createAIMealInfo(String mealTime,HttpServletRequest request);
}
