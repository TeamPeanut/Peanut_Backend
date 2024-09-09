package com.springboot.peanut.service;

import com.springboot.peanut.dto.food.FoodPredictResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FoodAIService {
    FoodPredictResponseDto FoodNamePredict(MultipartFile image)throws IOException;
}
