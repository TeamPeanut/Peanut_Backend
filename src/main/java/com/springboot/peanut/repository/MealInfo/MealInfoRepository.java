package com.springboot.peanut.repository.MealInfo;

import com.springboot.peanut.entity.MealInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealInfoRepository extends JpaRepository<MealInfo,Long>,MealInfoCustomRepository {
}
