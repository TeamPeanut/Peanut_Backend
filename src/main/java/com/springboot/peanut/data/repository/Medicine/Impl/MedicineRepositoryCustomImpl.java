package com.springboot.peanut.data.repository.Medicine.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.peanut.data.entity.*;
import com.springboot.peanut.data.repository.Medicine.MedicineRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicineRepositoryCustomImpl implements MedicineRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Medicine> findByTodayMedicineInfo(Long userId, LocalDate date) {
        QMedicine qMedicine = QMedicine.medicine;
        QUser qUser = QUser.user;

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(qMedicine)
                .leftJoin(qMedicine.user, qUser).fetchJoin() // Fetch Join 추가
                .where(qMedicine.user.id.eq(userId)
                        .and(qMedicine.create_At.eq(date)))
                .fetchOne());
    }

    @Override
    public Optional<Medicine> findMedicineByIdAndUserId(Long id, Long userId) {
        QMedicine qMedicine = QMedicine.medicine;
        QUser qUser = QUser.user;

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(qMedicine)
                .leftJoin(qMedicine.user, qUser).fetchJoin() // Fetch Join 추가
                .where(qMedicine.user.id.eq(userId)
                        .and(qMedicine.id.eq(id)))
                .fetchOne());
    }

    @Override
    public Optional<List<Medicine>> findByUserIdAndDate(Long userId, LocalDate date) {
        QMedicine qMedicine = QMedicine.medicine;
        QUser qUser = QUser.user;

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(qMedicine)
                .leftJoin(qMedicine.user, qUser).fetchJoin() // Fetch Join 추가
                .where(qMedicine.user.id.eq(userId)
                        .and(qMedicine.create_At.eq(date)))
                .fetch());
    }

    @Override
    public Optional<MedicineRecord> findMedicineRecordByUserIdAndDate(Long userId, LocalDate date) {
        QMedicineRecord qMedicineRecord = QMedicineRecord.medicineRecord;
        QUser qUser = QUser.user;

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(qMedicineRecord)
                .leftJoin(qMedicineRecord.user, qUser).fetchJoin() // Fetch Join 추가
                .where(qMedicineRecord.user.id.eq(userId)
                        .and(qMedicineRecord.recordDate.eq(date)))
                .fetchOne());
    }
}