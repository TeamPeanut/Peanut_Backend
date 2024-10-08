package com.springboot.peanut.service.Food;

import com.springboot.peanut.data.dto.food.FoodBloodSugarDto;
import com.springboot.peanut.data.dto.food.FoodCheckListDto;
import com.springboot.peanut.data.dto.food.FoodNameNutrionDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

public interface FoodCheckService {
    FoodCheckListDto getFoodCheckByDate(LocalDate date, HttpServletRequest request);
    FoodNameNutrionDto getFoodDetailByEatTime(LocalDate date, String eatTime, HttpServletRequest request);
    FoodBloodSugarDto getFoodFeedBackBloodSugarInfo(LocalDate date,String eatTime,HttpServletRequest request);
}
