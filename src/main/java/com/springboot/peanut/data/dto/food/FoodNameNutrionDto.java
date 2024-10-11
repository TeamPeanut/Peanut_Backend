package com.springboot.peanut.data.dto.food;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FoodNameNutrionDto {
    private double protein;
    private double carbohydrate;
    private double totalFat;
    private List<String> foodName ;
}
