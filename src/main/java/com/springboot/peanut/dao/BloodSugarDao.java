package com.springboot.peanut.dao;

import com.springboot.peanut.entity.CurrentBloodSugar;
import com.springboot.peanut.entity.FastingBloodSugar;

public interface BloodSugarDao {
    void saveFastingBloodSugar(FastingBloodSugar fastingBloodSugar);
    void saveCurrentBloodSugar(CurrentBloodSugar currentBloodSuger);
}
