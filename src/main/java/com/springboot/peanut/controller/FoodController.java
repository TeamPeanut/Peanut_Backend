package com.springboot.peanut.controller;

import com.springboot.peanut.dto.food.FoodDetailInfoDto;
import com.springboot.peanut.dto.food.FoodPredictResponseDto;
import com.springboot.peanut.dto.signDto.ResultDto;
import com.springboot.peanut.service.FoodAIService;
import com.springboot.peanut.service.FoodDetailService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/food")
@RequiredArgsConstructor
public class FoodController {
    private final FoodAIService foodAIService;
    private final FoodDetailService foodDetailService;

    @PostMapping("/predict")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<FoodPredictResponseDto> BlockPlay(
            @RequestPart("foodImage") MultipartFile foodImage) throws IOException {

        FoodPredictResponseDto results = foodAIService.FoodNamePredict(foodImage);
        return ResponseEntity.status(HttpStatus.OK).body(results);

    }

    @GetMapping("/details")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<List<FoodDetailInfoDto>> getFoodDetailInfo(@RequestParam  List<String> name, HttpServletRequest request){
        List<FoodDetailInfoDto> foodDetailInfoDto = foodDetailService.getFoodDetailInfo(name,request);
        return ResponseEntity.status(HttpStatus.OK).body(foodDetailInfoDto);
    }

    @PostMapping("/save-meal")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<ResultDto> createMealInfo(String mealTime, HttpServletRequest request){
        ResultDto resultDto = foodDetailService.createMealInfo(mealTime,request);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }

}
