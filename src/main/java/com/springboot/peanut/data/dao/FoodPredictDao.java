package com.springboot.peanut.data.dao;

import com.springboot.peanut.data.entity.FoodPredict;

public interface FoodPredictDao {

    void saveFoodPredictResults(FoodPredict foodPredict);
}
