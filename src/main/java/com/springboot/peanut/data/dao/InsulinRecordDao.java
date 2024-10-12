package com.springboot.peanut.data.dao;

import com.springboot.peanut.data.entity.InsulinRecord;

import java.time.LocalDate;
import java.util.Optional;

public interface InsulinRecordDao {
    void saveInsulinRecord(InsulinRecord insulinRecord);
    Optional<InsulinRecord> findInsulinRecordByUserId(Long userId, LocalDate date);
}
