package com.springboot.peanut.data.repository.FoodNutrition;

import com.springboot.peanut.data.entity.FoodNutrition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FoodNutritionRepository extends JpaRepository<FoodNutrition,Long>,FoodNutritionRepositoryCustom {
    List<FoodNutrition> findAllById(Long foodNutritionIds);
    Optional<FoodNutrition> findByName(String name);

}
