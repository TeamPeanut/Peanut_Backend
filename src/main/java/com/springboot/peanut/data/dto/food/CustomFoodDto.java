package com.springboot.peanut.data.dto.food;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomFoodDto {
    private String foodName; // 사용자가 추가한 음식 이름
    private int servingCount; // 해당 음식의 인분 수

    @Override
    public String toString() {
        return "CustomFood{" +
                "foodName='" + foodName + '\'' +
                ", servingCount=" + servingCount +
                '}';
    }
}
