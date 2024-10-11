package com.springboot.peanut.service.MainPage;

import com.springboot.peanut.data.dto.food.FoodAllDetailDto;
import com.springboot.peanut.data.dto.mainPage.MedicineInsulinStatusRequestDto;
import com.springboot.peanut.data.dto.mainPage.PatientMainPageGetAdditionalInfoDto;
import com.springboot.peanut.data.dto.mainPage.MainPageGetUserDto;
import com.springboot.peanut.data.dto.signDto.ResultDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

public interface PatientMainPageService {
    MainPageGetUserDto getUserInfoMainPage(HttpServletRequest request);
    PatientMainPageGetAdditionalInfoDto getAdditionalInfoMainPage(HttpServletRequest request,LocalDate date) ;
    ResultDto saveMedicineInsulinStatus(HttpServletRequest request, LocalDate date, MedicineInsulinStatusRequestDto medicineInsulinStatusRequestDto) ;
    FoodAllDetailDto getFoodAllDetail(LocalDate date ,HttpServletRequest request);
    FoodAllDetailDto getFoodDetailByEatTime(LocalDate date,String eatTime,HttpServletRequest request);



    }
