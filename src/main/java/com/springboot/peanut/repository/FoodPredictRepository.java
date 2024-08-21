package com.springboot.peanut.repository;

import com.springboot.peanut.entity.FoodPredict;
import com.springboot.peanut.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodPredictRepository extends JpaRepository<FoodPredict,Long> {

}
