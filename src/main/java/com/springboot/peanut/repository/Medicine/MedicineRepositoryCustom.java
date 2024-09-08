package com.springboot.peanut.repository.Medicine;

import com.springboot.peanut.entity.Medicine;

import java.time.LocalDate;
import java.util.Optional;

public interface MedicineRepositoryCustom {
    Optional<Medicine> findByTodayMedicineInfo(Long userId, LocalDate date);
}
