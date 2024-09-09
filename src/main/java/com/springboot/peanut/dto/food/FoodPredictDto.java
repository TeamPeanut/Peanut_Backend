package com.springboot.peanut.dto.food;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FoodPredictDto {
    private String foodName;

    private String accuracy;
}
