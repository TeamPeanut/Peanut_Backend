package com.springboot.peanut.service.MainPage;

import com.springboot.peanut.dto.food.FoodAllDetailDto;
import com.springboot.peanut.dto.mainPage.GuardianMainPageGetAdditionalInfoDto;
import com.springboot.peanut.dto.mainPage.MainPageGetUserDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

public interface GuardianMainPageService {
    MainPageGetUserDto getUserInfoMainPage(HttpServletRequest request);
    GuardianMainPageGetAdditionalInfoDto getAdditionalInfoMainPage(HttpServletRequest request, LocalDate date);
    FoodAllDetailDto getFoodAllDetail(LocalDate date ,HttpServletRequest request);
    FoodAllDetailDto getFoodDetailByEatTime(LocalDate date,String eatTime,HttpServletRequest request);



}
