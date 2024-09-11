package com.springboot.peanut.dto.food;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FoodAllDetailDto {

    private double carbohydrate;   // 탄수화물 (g)
    private double protein;        // 단백질 (g)
    private double fat;            // 지방 (g)

}
