package com.springboot.peanut.data.dao;

import com.springboot.peanut.data.entity.Intake;

import java.util.List;

public interface IntakeDao {
    void saveIntakeInfo(Intake intake);
    List<String> findIntakeTime(Long userId);
}
