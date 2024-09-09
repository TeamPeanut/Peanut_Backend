package com.springboot.peanut.repository;

import com.springboot.peanut.entity.MealInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<MealInfo,Long> {
}
