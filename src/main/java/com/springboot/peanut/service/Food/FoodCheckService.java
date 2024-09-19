package com.springboot.peanut.service.Food;

import com.springboot.peanut.dto.food.FoodCheckDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

public interface FoodCheckService {
    FoodCheckDto getFoodCheckByDate(LocalDate date, HttpServletRequest request);
}
