package com.springboot.peanut.service.MainPage;

import com.springboot.peanut.data.dto.food.FoodAllDetailDto;
import com.springboot.peanut.data.dto.mainPage.GuardianMainPageGetAdditionalInfoDto;
import com.springboot.peanut.data.dto.mainPage.MainPageGetUserDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

public interface PatientMainService {
    MainPageGetUserDto getUserInfoMainPage(HttpServletRequest request);
    GuardianMainPageGetAdditionalInfoDto getAdditionalInfoMainPage(HttpServletRequest request, LocalDate date);
    FoodAllDetailDto getFoodAllDetail(LocalDate date , HttpServletRequest request);
    FoodAllDetailDto getFoodDetailByEatTime(LocalDate date,String eatTime,HttpServletRequest request);

}
