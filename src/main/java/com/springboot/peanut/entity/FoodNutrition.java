package com.springboot.peanut.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoodNutrition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;           // 음식 이름 (한글)
    private String englishName;    // 음식 이름 (영문)

    private double carbohydrate;   // 탄수화물 (g)
    private double protein;        // 단백질 (g)
    private double fat;            // 지방 (g)
    private double cholesterol;    // 콜레스테롤 (mg)
    private double giIndex;        // GI 지수
    private double glIndex;        // GL 지수

}