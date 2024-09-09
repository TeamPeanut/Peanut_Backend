package com.springboot.peanut.dto.foodPredict;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FoodDetailInfoDto {

    private String name;           // 음식 이름 (한글)
    private double carbohydrate;   // 탄수화물 (g)
    private double protein;        // 단백질 (g)
    private double fat;            // 지방 (g)
    private double cholesterol;    // 콜레스테롤 (mg)
    private double giIndex;        // GI 지수
    private double glIndex;        // GL 지수
    private double expectationBloodSugar; // 예상 당수치



}
