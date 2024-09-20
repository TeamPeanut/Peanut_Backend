package com.springboot.peanut.data.repository.Medicine;

import com.springboot.peanut.data.entity.Medicine;

import java.time.LocalDate;
import java.util.Optional;

public interface MedicineRepositoryCustom {
    Optional<Medicine> findByTodayMedicineInfo(Long userId, LocalDate date);
}
