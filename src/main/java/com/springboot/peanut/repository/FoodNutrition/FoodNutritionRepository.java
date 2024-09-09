package com.springboot.peanut.repository.FoodNutrition;

import com.springboot.peanut.entity.FoodNutrition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodNutritionRepository extends JpaRepository<FoodNutrition,Long>,FoodNutritionRepositoryCustom {
}
