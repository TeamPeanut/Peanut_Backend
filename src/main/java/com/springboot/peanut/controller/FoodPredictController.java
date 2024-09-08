package com.springboot.peanut.controller;

import com.springboot.peanut.dto.foodPredict.FoodPredictResponseDto;
import com.springboot.peanut.service.FoodAIService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/predict-api")
@RequiredArgsConstructor
public class FoodPredictController {
    private final FoodAIService foodAIService;

    @PostMapping("/food")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<FoodPredictResponseDto> BlockPlay(
            @RequestPart("foodImage") MultipartFile foodImage) throws IOException {

        FoodPredictResponseDto results = foodAIService.FoodNamePredict(foodImage);
        return ResponseEntity.status(HttpStatus.OK).body(results);

    }

}
