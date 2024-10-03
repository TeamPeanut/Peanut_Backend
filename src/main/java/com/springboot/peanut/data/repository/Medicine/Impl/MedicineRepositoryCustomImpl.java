package com.springboot.peanut.data.repository.Medicine.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.peanut.data.entity.Medicine;
import com.springboot.peanut.data.entity.QMedicine;
import com.springboot.peanut.data.repository.Medicine.MedicineRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class MedicineRepositoryCustomImpl implements MedicineRepositoryCustom {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Medicine> findByTodayMedicineInfo(Long userId, LocalDate date) {
        QMedicine qMedicine = QMedicine.medicine;

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(qMedicine)
                .where(qMedicine.user.id.eq(userId)
                        .and(qMedicine.create_At.eq(date)))
                .fetchOne());
    }
}
