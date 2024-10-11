package com.springboot.peanut.data.dto.food;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FoodDetailInfoDto {
    private Long foodId;
    private String name;           // 음식 이름 (한글)
    private double carbohydrate;   // 탄수화물 (g)
    private double protein;        // 단백질 (g)
    private double fat;            // 지방 (g)
    private double cholesterol;    // 콜레스테롤 (mg)
    private double giIndex;        // GI 지수
    private double glIndex;
    private double expectedBloodSugar; // GL 지수
    private int servingCount;

}
