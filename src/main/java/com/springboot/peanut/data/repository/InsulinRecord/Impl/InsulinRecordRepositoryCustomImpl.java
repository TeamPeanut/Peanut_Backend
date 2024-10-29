package com.springboot.peanut.data.repository.InsulinRecord.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.peanut.data.entity.Insulin;
import com.springboot.peanut.data.entity.InsulinRecord;
import com.springboot.peanut.data.entity.QInsulinRecord;
import com.springboot.peanut.data.repository.InsulinRecord.InsulinRecordRepository;
import com.springboot.peanut.data.repository.InsulinRecord.InsulinRecordRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

    @Override
    public List<InsulinRecord> findInsulinByYearAndMonth(Long userId, int year, int month) {
        QInsulinRecord qInsulinRecord = QInsulinRecord.insulinRecord;

        return jpaQueryFactory
                .selectFrom(qInsulinRecord)
                .where(qInsulinRecord.user.id.eq(userId)
                        .and(qInsulinRecord.recordDate.year().eq(year))
                        .and(qInsulinRecord.recordDate.month().eq(month)))
                .fetch();
    }


}