package com.springboot.peanut.service.Food.Impl;

import com.springboot.peanut.jwt.JwtProvider;
import com.springboot.peanut.repository.MealInfo.MealInfoRepository;
import com.springboot.peanut.service.Food.FoodCheckService;
import com.springboot.peanut.service.Jwt.JwtAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FoodCheckServiceImpl implements FoodCheckService {
    private final JwtAuthenticationService jwtAuthenticationService;
    private final MealInfoRepository mealInfoRepository;

}
