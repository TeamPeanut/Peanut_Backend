package com.springboot.peanut.service.Food;

import com.springboot.peanut.dto.food.FoodCheckDto;
import com.springboot.peanut.dto.food.FoodCheckListDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

public interface FoodCheckService {
    FoodCheckListDto getFoodCheckByDate(LocalDate date, HttpServletRequest request);
}
