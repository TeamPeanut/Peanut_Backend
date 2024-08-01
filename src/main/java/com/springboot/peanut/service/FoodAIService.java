package com.springboot.peanut.service;

import com.springboot.peanut.dto.FoodPredictResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FoodAIService {
    FoodPredictResponseDto foodPredict(MultipartFile image)throws IOException;
}
