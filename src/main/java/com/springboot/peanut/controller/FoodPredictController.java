package com.springboot.peanut.controller;

import com.springboot.peanut.dto.FoodPredictResponseDto;
import com.springboot.peanut.service.FoodAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/api/predict-api")
@RequiredArgsConstructor
public class FoodPredictController {
    private final FoodAIService foodAIService;

    @PostMapping("/food")
    public ResponseEntity<FoodPredictResponseDto> BlockPlay(
            @RequestPart("foodImage") MultipartFile foodImage) throws IOException {

        FoodPredictResponseDto results = foodAIService.foodPredict(foodImage);
        return ResponseEntity.status(HttpStatus.OK).body(results);

    }

}
