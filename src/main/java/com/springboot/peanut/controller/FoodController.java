package com.springboot.peanut.controller;

import com.springboot.peanut.dto.food.FoodDetailInfoDto;
import com.springboot.peanut.dto.food.FoodNutritionDto;
import com.springboot.peanut.dto.food.FoodPredictResponseDto;
import com.springboot.peanut.dto.signDto.ResultDto;
import com.springboot.peanut.service.FoodAIService;
import com.springboot.peanut.service.FoodRecordAIService;
import com.springboot.peanut.service.FoodRecordNormalService;
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
    private final FoodRecordAIService foodRecordAIService;
    private final FoodRecordNormalService foodRecordNormalService;


    @PostMapping("/ai/predict")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<FoodPredictResponseDto> BlockPlay(
            @RequestPart("foodImage") MultipartFile foodImage) throws IOException {

        FoodPredictResponseDto results = foodAIService.FoodNamePredict(foodImage);
        return ResponseEntity.status(HttpStatus.OK).body(results);

    }

    @GetMapping("/ai/details")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<List<FoodDetailInfoDto>> getFoodDetailInfo(@RequestParam  List<String> name, HttpServletRequest request){
        List<FoodDetailInfoDto> foodDetailInfoDto = foodRecordAIService.getFoodDetailInfo(name,request);
        return ResponseEntity.status(HttpStatus.OK).body(foodDetailInfoDto);
    }

    @PostMapping("/ai/save-meal")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<ResultDto> createAIMealInfo(String mealTime, HttpServletRequest request){
        ResultDto resultDto = foodRecordAIService.createAIMealInfo(mealTime,request);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }

    @GetMapping("/normal/details")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<List<FoodNutritionDto>>getFoodNutritionByName(@RequestParam List<String> name, HttpServletRequest request){
        List<FoodNutritionDto> foodNutritionByName = foodRecordNormalService.getFoodNutritionByName(name,request);
        return ResponseEntity.status(HttpStatus.OK).body(foodNutritionByName);
    }

    @PostMapping("/normal/save-meal")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    ResponseEntity<ResultDto> saveNormalMealInfo(String mealTime, int servingCount, HttpServletRequest request) {
        ResultDto resultDto = foodRecordNormalService.saveNormalMealInfo(mealTime,servingCount,request);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }


    }
