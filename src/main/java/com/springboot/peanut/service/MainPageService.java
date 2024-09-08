package com.springboot.peanut.service;

import com.springboot.peanut.dto.mainPage.MainPageGetUserDto;

import javax.servlet.http.HttpServletRequest;

public interface MainPageService {
    MainPageGetUserDto getUserInfoMainPage(HttpServletRequest request);

}
