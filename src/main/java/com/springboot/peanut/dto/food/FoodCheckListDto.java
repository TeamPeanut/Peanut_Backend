package com.springboot.peanut.dto.food;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodCheckListDto {
    private List<FoodCheckDto> foodCheckList;
}