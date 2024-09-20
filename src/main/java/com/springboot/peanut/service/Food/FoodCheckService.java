package com.springboot.peanut.service.Food;

import com.springboot.peanut.data.dto.food.FoodCheckListDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

public interface FoodCheckService {
    FoodCheckListDto getFoodCheckByDate(LocalDate date, HttpServletRequest request);
}
