package com.springboot.peanut.data.repository;

import com.springboot.peanut.data.entity.FoodPredict;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodPredictRepository extends JpaRepository<FoodPredict,Long> {

}
