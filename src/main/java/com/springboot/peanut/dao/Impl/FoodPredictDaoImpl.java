package com.springboot.peanut.dao.Impl;

import com.springboot.peanut.dao.FoodPredictDao;
import com.springboot.peanut.entity.FoodPredict;
import com.springboot.peanut.repository.FoodPredictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FoodPredictDaoImpl implements FoodPredictDao {

    private final FoodPredictRepository foodPredictRepository;

    @Override
    public void saveFoodPredictResults(FoodPredict foodPredict){
        foodPredictRepository.save(foodPredict);
    }

}
