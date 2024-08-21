package com.springboot.peanut.dto.foodPredict;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FoodPredictResponseDto {
    private List<FoodPredictDto> predictions;
    private String image_url;
}
