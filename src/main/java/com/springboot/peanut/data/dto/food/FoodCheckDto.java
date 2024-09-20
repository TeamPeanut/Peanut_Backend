package com.springboot.peanut.data.dto.food;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FoodCheckDto {

    private List<String> foodName;

    private String imageUrl;

    private String eatTime;

    private String feedBack;
}
