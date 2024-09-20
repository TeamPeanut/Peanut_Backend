package com.springboot.peanut.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class MealInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eatTime;

    private double expectedBloodSugar;

    private String imageUrl;

    @ManyToMany
    @JoinTable(
            name = "meal_info_food_nutrition", // 연결 테이블 명시
            joinColumns = @JoinColumn(name = "meal_info_id"),
            inverseJoinColumns = @JoinColumn(name = "food_nutrition_id")
    )
    private List<FoodNutrition> foodNutritionList = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    private LocalDate create_At;


    // 생성자 추가
    public static MealInfo MealInfo(String eatTime, String imageUrl,double expectedBloodSugar, List<FoodNutrition> foodNutritionList, User user) {
        MealInfo mealInfo = new MealInfo();
        mealInfo.eatTime = eatTime;
        mealInfo.expectedBloodSugar = expectedBloodSugar;
        mealInfo.foodNutritionList = foodNutritionList;
        mealInfo.imageUrl = imageUrl;
        mealInfo.user = user;
        mealInfo.create_At = LocalDate.now();
        return mealInfo;
    }
}

