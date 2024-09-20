package com.springboot.peanut.data.dao.Impl;

import com.springboot.peanut.data.dao.MealDao;
import com.springboot.peanut.data.entity.MealInfo;
import com.springboot.peanut.data.repository.MealRepository;
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
