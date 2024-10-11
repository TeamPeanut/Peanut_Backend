package com.springboot.peanut.data.repository.InsulinRecord;

import com.springboot.peanut.data.entity.InsulinRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface InsulinRecordRepository extends JpaRepository<InsulinRecord,Long>,InsulinRecordRepositoryCustom {
    Optional<InsulinRecord> findInsulinRecordByUserId(Long userId);
}
