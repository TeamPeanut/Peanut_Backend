package com.springboot.peanut.data.dto.food;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealResponseDto {
    private List<String> mealTime;
    private String expectedBloodSugar;
    private List<Long> foodNutritionIds;
}
