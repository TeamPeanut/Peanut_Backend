package com.springboot.peanut.data.repository.InsulinRecord;

import com.springboot.peanut.data.entity.InsulinRecord;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface InsulinRecordRepositoryCustom {
    Optional<InsulinRecord> findInsulinRecordByUserIdAndDate(Long userId, LocalDate date) ;
    }
