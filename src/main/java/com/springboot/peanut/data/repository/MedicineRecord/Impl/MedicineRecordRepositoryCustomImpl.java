package com.springboot.peanut.data.repository.MedicineRecord.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.peanut.data.entity.*;
import com.springboot.peanut.data.repository.MedicineRecord.MedicineRecordRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicineRecordRepositoryCustomImpl implements MedicineRecordRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public  Optional<List<MedicineRecord>> findMedicineRecordByUserId(Long userId, LocalDate date) {
        QMedicineRecord qMedicineRecord = QMedicineRecord.medicineRecord;

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(qMedicineRecord)
                .where(qMedicineRecord.user.id.eq(userId)
                        .and(qMedicineRecord.recordDate.eq(date)))
                .fetch());
    }
    @Override
    public List<MedicineRecord> findMedicineByYearAndMonth(Long userId, int year, int month) {
        QMedicineRecord qMedicineRecord = QMedicineRecord.medicineRecord;

        return jpaQueryFactory
                .selectFrom(qMedicineRecord)
                .where(qMedicineRecord.user.id.eq(userId)
                        .and(qMedicineRecord.recordDate.year().eq(year))
                        .and(qMedicineRecord.recordDate.month().eq(month)))
                .fetch();
    }


}
