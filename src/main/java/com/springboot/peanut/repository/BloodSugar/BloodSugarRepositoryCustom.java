package com.springboot.peanut.repository.BloodSugar;

import com.springboot.peanut.entity.BloodSugar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BloodSugarRepositoryCustom {
Optional<BloodSugar> findFastingBloodSugar(Long userId);
Optional<BloodSugar> findClosestBloodSugar(Long userId);
List<BloodSugar>findTodayBloodSugar(Long userId, LocalDate date);

}
