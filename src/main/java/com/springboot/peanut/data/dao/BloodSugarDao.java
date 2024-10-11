package com.springboot.peanut.data.dao;

import com.springboot.peanut.data.entity.BloodSugar;
import com.springboot.peanut.data.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface BloodSugarDao {
    void saveBloodSugar(BloodSugar bloodSugar);
    List<BloodSugar> findByUserAndMonth(Long userId, int year, int month);
    Optional<BloodSugar>  findFastingBloodSugar(Long userId);
    Optional<BloodSugar> findClosestBloodSugar(Long userId);
    List<BloodSugar> findTodayBloodSugar(Long id, LocalDate date);
}
