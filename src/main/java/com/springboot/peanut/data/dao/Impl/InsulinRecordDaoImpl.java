package com.springboot.peanut.data.dao.Impl;

import com.springboot.peanut.data.dao.InsulinRecordDao;
import com.springboot.peanut.data.entity.InsulinRecord;
import com.springboot.peanut.data.repository.InsulinRecord.InsulinRecordRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InsulinRecordDaoImpl implements InsulinRecordDao {

    private final InsulinRecordRepository insulinRecordRepository;

    @Override
    public void saveInsulinRecord(InsulinRecord insulinRecord) {
        insulinRecordRepository.save(insulinRecord);
    }

    @Override
    public Optional<InsulinRecord> findInsulinRecordByUserId(Long userId, LocalDate date) {
        return insulinRecordRepository.findInsulinRecordByUserIdAndDate(userId,date);
    }
}
