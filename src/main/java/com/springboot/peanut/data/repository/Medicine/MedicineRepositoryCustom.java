package com.springboot.peanut.data.repository.Medicine;

import com.springboot.peanut.data.entity.Insulin;
import com.springboot.peanut.data.entity.Medicine;
import com.springboot.peanut.data.entity.MedicineRecord;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface MedicineRepositoryCustom {
    Optional<Medicine> findByTodayMedicineInfo(Long userId, LocalDate date);

    Optional<List<Medicine>> findByUserIdAndDate(Long userId, LocalDate date);
    Optional<MedicineRecord> findMedicineRecordByUserIdAndDate(Long userId, LocalDate date);

}
