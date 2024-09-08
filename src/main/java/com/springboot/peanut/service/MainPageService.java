package com.springboot.peanut.service;

import com.springboot.peanut.dto.mainPage.MainPageGetAdditionalInfoDto;
import com.springboot.peanut.dto.mainPage.MainPageGetUserDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

public interface MainPageService {
    MainPageGetUserDto getUserInfoMainPage(HttpServletRequest request);
    MainPageGetAdditionalInfoDto getAdditionalInfoMainPage(HttpServletRequest request, LocalDate date);


}
