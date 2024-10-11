package com.springboot.peanut.data.repository.FoodNutrition.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.peanut.data.entity.FoodNutrition;
import com.springboot.peanut.data.entity.QFoodNutrition;
import com.springboot.peanut.data.repository.FoodNutrition.FoodNutritionRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodNutritionRepositoryCustomImpl implements FoodNutritionRepositoryCustom {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public List<FoodNutrition> findFoodNutritionByFoodName(List<String> foodName) {
        QFoodNutrition qFoodNutrition = QFoodNutrition.foodNutrition;

        return jpaQueryFactory
                .selectFrom(qFoodNutrition)
                .where(qFoodNutrition.englishName.in(foodName)
                        .or(qFoodNutrition.name.in(foodName)))
                .fetch();
    }

    @Override
    public List<FoodNutrition> findFoodNutritionByFoodNameKor(List<String> foodName) {
        QFoodNutrition qFoodNutrition = QFoodNutrition.foodNutrition;

        return  jpaQueryFactory
                .selectFrom(qFoodNutrition)
                .where(qFoodNutrition.name.in(foodName))
                .fetch();
    }
}
