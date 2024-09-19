package com.springboot.peanut.service.Food;

import com.springboot.peanut.dto.food.FoodDetailInfoDto;
import com.springboot.peanut.dto.food.FoodPredictResponseDto;
import com.springboot.peanut.dto.signDto.ResultDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface FoodAIService {
    FoodPredictResponseDto FoodNamePredict(MultipartFile image,HttpServletRequest request)throws IOException;
    List<FoodDetailInfoDto> getFoodDetailInfo(List<String> name , HttpServletRequest request);

    ResultDto createAIMealInfo(String mealTime, HttpServletRequest request);

}
