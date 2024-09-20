package com.springboot.peanut.data.repository.BloodSugar;

import com.springboot.peanut.data.entity.BloodSugar;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BloodSugarRepositoryCustom {
Optional<BloodSugar> findFastingBloodSugar(Long userId);
Optional<BloodSugar> findClosestBloodSugar(Long userId);
List<BloodSugar>findTodayBloodSugar(Long userId, LocalDate date);

}
