package com.springboot.peanut.data.repository.Medicine;

import com.springboot.peanut.data.entity.Medicine;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public interface MedicineRepositoryCustom {
    Optional<Medicine> findByTodayMedicineInfo(Long userId, LocalDate date);

    Optional<Medicine> findMedicationInfoAndMedicationTimeForUser(Long userId, LocalDate date);

}
