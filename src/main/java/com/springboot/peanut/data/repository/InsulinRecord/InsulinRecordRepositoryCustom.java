package com.springboot.peanut.data.repository.InsulinRecord;

import com.springboot.peanut.data.entity.Insulin;
import com.springboot.peanut.data.entity.InsulinRecord;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InsulinRecordRepositoryCustom {
    Optional<InsulinRecord> findInsulinRecordByUserIdAndDate(Long userId, LocalDate date) ;
    List<InsulinRecord> findInsulinByYearAndMonth(Long userId, int year, int month);

}
