package com.springboot.peanut.data.dao;

import com.springboot.peanut.data.entity.InsulinRecord;
import com.springboot.peanut.data.entity.Medicine;
import com.springboot.peanut.data.entity.MedicineRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MedicineRecordDao {
    void saveMedicineRecord(MedicineRecord medicineRecord);
    Optional<List<MedicineRecord>>  findMedicineRecordByUserId(Long userId, LocalDate date);
    List<MedicineRecord> findMedicineByYearAndMonth(Long userId, int year, int month) ;

}
