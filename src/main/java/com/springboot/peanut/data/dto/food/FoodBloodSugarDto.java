package com.springboot.peanut.data.dto.food;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class FoodBloodSugarDto {
    private double beforeBloodSugar;
    private double afterBloodSugar;
    private String msg;
}
