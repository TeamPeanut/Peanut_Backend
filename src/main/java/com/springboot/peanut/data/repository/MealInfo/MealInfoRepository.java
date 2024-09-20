package com.springboot.peanut.data.repository.MealInfo;

import com.springboot.peanut.data.entity.MealInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealInfoRepository extends JpaRepository<MealInfo,Long>,MealInfoCustomRepository {
}
