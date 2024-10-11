package com.springboot.peanut.data.repository.MedicineRecord;

import com.springboot.peanut.data.entity.MedicineRecord;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface MedicineRecordRepositoryCustom {
    Optional<List<MedicineRecord>> findMedicineRecordByUserId(Long userId, LocalDate date);
}
