package com.springboot.peanut.repository.Medicine.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.peanut.entity.Medicine;
import com.springboot.peanut.entity.QMedicine;
import com.springboot.peanut.repository.Medicine.MedicineRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class MedicineRepositoryCustomImpl implements MedicineRepositoryCustom {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Medicine> findByTodayMedicineInfo(Long userId, LocalDate date) {
        QMedicine qMedicine = QMedicine.medicine;

        LocalDate today = LocalDate.now();

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(qMedicine)
                .where(qMedicine.user.id.eq(userId)
                        .and(qMedicine.create_At.eq(today)))
                .fetchOne());
    }
}
