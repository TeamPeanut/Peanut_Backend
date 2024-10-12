package com.springboot.peanut.data.repository.InsulinRecord.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.peanut.data.entity.InsulinRecord;
import com.springboot.peanut.data.entity.QInsulinRecord;
import com.springboot.peanut.data.repository.InsulinRecord.InsulinRecordRepository;
import com.springboot.peanut.data.repository.InsulinRecord.InsulinRecordRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;



@Service
@RequiredArgsConstructor
public class InsulinRecordRepositoryCustomImpl implements InsulinRecordRepositoryCustom {

        private final JPAQueryFactory jpaQueryFactory;

        @Override
        public Optional<InsulinRecord> findInsulinRecordByUserIdAndDate(Long userId, LocalDate date) {
            QInsulinRecord qInsulinRecord = QInsulinRecord.insulinRecord;
            return Optional.ofNullable(jpaQueryFactory
                    .selectFrom(qInsulinRecord)
                    .where(qInsulinRecord.user.id.eq(userId)
                            .and(qInsulinRecord.recordDate.eq(date)))
                    .fetchOne());
        }
    }