package com.springboot.peanut.data.repository.FoodNutrition;

import com.springboot.peanut.data.entity.FoodNutrition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodNutritionRepository extends JpaRepository<FoodNutrition,Long>,FoodNutritionRepositoryCustom {
    List<FoodNutrition> findAllById(Long foodNutritionIds);

}
