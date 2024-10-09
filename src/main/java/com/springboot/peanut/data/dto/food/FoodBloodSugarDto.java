package com.springboot.peanut.data.dto.food;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class FoodBloodSugarDto {
    private Map<String,LocalDateTime> beforeBloodSugar;
    private Map<Double, LocalTime>  afterBloodSugar;
    private String msg;
}
