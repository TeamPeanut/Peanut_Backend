package com.springboot.peanut.data.repository.MealInfo;

import com.springboot.peanut.data.entity.MealInfo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MealInfoCustomRepository {
    Optional<List<MealInfo>> getByUserAllMealInfo(LocalDate date,Long userId);
    Optional<MealInfo> getMealInfoByEatTime(LocalDate date,Long id,String eatTime);

}
