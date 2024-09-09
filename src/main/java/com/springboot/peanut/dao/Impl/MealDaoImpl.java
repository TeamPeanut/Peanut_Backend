package com.springboot.peanut.dao.Impl;

import com.springboot.peanut.dao.MealDao;
import com.springboot.peanut.entity.MealInfo;
import com.springboot.peanut.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MealDaoImpl implements MealDao {
    private final MealRepository mealRepository;
    @Override
    public void save(MealInfo mealInfo) {
        mealRepository.save(mealInfo);
    }

}
