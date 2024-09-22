package com.springboot.peanut.controller;

import com.springboot.peanut.data.dto.food.FoodCheckListDto;
import com.springboot.peanut.data.dto.food.FoodDetailInfoDto;
import com.springboot.peanut.data.dto.food.FoodNutritionDto;
import com.springboot.peanut.data.dto.food.FoodPredictResponseDto;
import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.service.Food.FoodAIService;
import com.springboot.peanut.service.Food.FoodCheckService;
import com.springboot.peanut.service.Food.FoodRecordNormalService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/food")
@RequiredArgsConstructor
public class FoodController {
    private final FoodAIService foodAIService;
    private final FoodRecordNormalService foodRecordNormalService;
    private final FoodCheckService foodCheckService;


    @PostMapping("/ai/predict")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<FoodPredictResponseDto> BlockPlay(
            @RequestPart("foodImage") MultipartFile foodImage,HttpServletRequest request) throws IOException {

        FoodPredictResponseDto results = foodAIService.FoodNamePredict(foodImage,request);
        return ResponseEntity.status(HttpStatus.OK).body(results);

    }

    @GetMapping("/ai/details")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<List<FoodDetailInfoDto>> getFoodDetailInfo(HttpServletRequest request){
        List<FoodDetailInfoDto> foodDetailInfoDto = foodAIService.getFoodDetailInfo(request);
        return ResponseEntity.status(HttpStatus.OK).body(foodDetailInfoDto);
    }

    @PostMapping("/ai/save-meal")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<ResultDto> createAIMealInfo(String mealTime, HttpServletRequest request){
        ResultDto resultDto = foodAIService.createAIMealInfo(mealTime,request);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }

    @PostMapping("/normal/save-image")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<ResultDto> saveNormalMealInfoImage(@RequestPart("foodImage") MultipartFile foodImage, HttpServletRequest request) throws IOException {
        ResultDto resultDto = foodRecordNormalService.saveNormalMealInfoImage(foodImage,request);
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
    ResponseEntity<ResultDto> saveNormalMealInfo(String mealTime, @RequestParam List<Integer> servingCount, HttpServletRequest request) {
        ResultDto resultDto = foodRecordNormalService.saveNormalMealInfo(mealTime,servingCount,request);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }
    @GetMapping("/food-record-check")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<FoodCheckListDto> getFoodCheckByDate(@RequestParam("date")@DateTimeFormat(iso= DateTimeFormat.ISO.DATE) LocalDate date, HttpServletRequest request) {
        FoodCheckListDto foodCheckDto = foodCheckService.getFoodCheckByDate(date,request);
        return ResponseEntity.status(HttpStatus.OK).body(foodCheckDto);
    }
    @GetMapping("/add-food")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<ResultDto> addCustomFood(String foodName, int servingCount, HttpServletRequest request) {
        ResultDto resultDto = foodAIService.addCustomFood(foodName,servingCount,request);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
        }
    }
