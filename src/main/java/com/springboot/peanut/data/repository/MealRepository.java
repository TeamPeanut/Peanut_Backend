package com.springboot.peanut.data.repository;

import com.springboot.peanut.data.entity.MealInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<MealInfo,Long> {
}
