package com.springboot.peanut.data.dao.Impl;

import com.springboot.peanut.data.dao.FoodPredictDao;
import com.springboot.peanut.data.entity.FoodPredict;
import com.springboot.peanut.data.repository.FoodPredictRepository;
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
