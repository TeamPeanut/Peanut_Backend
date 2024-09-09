package com.springboot.peanut.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "meal_food_nutrition",  // 중간 테이블 생성
            joinColumns = @JoinColumn(name = "meal_info_id"),
            inverseJoinColumns = @JoinColumn(name = "food_nutrition_id")
    )
    private List<FoodNutrition> foodNutritionList = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public void addFoodNutrition(FoodNutrition foodNutrition) {
        foodNutritionList.add(foodNutrition);
    }

    // 생성자 추가
    public static MealInfo MealInfo(String eatTime, double expectedBloodSugar, List<FoodNutrition> foodNutritionList, User user) {
        MealInfo mealInfo = new MealInfo();
        mealInfo.eatTime = eatTime;
        mealInfo.expectedBloodSugar = expectedBloodSugar;
        mealInfo.foodNutritionList = foodNutritionList;
        mealInfo.user = user;
        return mealInfo;
    }
}

