package com.springboot.peanut.repository.MealInfo;

import com.springboot.peanut.entity.MealInfo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MealInfoCustomRepository {
    Optional<List<MealInfo>> getByUserAllMealInfo(LocalDate date,Long userId);
    Optional<MealInfo> getMealInfoByEatTime(LocalDate date,Long id,String eatTime);

}
