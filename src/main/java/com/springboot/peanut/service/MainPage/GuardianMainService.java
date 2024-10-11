package com.springboot.peanut.service.MainPage;

import com.springboot.peanut.data.dto.food.FoodAllDetailDto;
import com.springboot.peanut.data.dto.mainPage.GuardianMainPageGetAdditionalInfoDto;
import com.springboot.peanut.data.dto.mainPage.MainPageGetUserDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

public interface GuardianMainService {
    MainPageGetUserDto getPatientUserInfoMainPage(HttpServletRequest request);
    GuardianMainPageGetAdditionalInfoDto getPatientAdditionalInfoMainPage(HttpServletRequest request, LocalDate date);
    FoodAllDetailDto getPatientFoodAllDetail(LocalDate date , HttpServletRequest request);
    FoodAllDetailDto getPatientFoodDetailByEatTime(LocalDate date,String eatTime,HttpServletRequest request);

}
