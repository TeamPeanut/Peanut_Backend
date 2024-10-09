package com.springboot.peanut.data.repository.Medicine.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.peanut.data.entity.Medicine;
import com.springboot.peanut.data.entity.QIntake;
import com.springboot.peanut.data.entity.QMedicine;
import com.springboot.peanut.data.repository.Medicine.MedicineRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
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

    @Override
    public Optional<Medicine> findMedicationInfoAndMedicationTimeForUser(Long userId, LocalDate date) {
        QMedicine qMedicine = QMedicine.medicine;
        QIntake qIntake = QIntake.intake;

        // 현재 시간 가져오기
        LocalTime currentTime = LocalTime.now();
        String measureTime;

        // 시간대에 따라 measureTime 설정
        if (currentTime.isAfter(LocalTime.of(6, 0)) && currentTime.isBefore(LocalTime.of(11, 0))) {
            measureTime = "아침"; // 아침 전/후 모두 포함
        } else if (currentTime.isAfter(LocalTime.of(11, 0)) && currentTime.isBefore(LocalTime.of(17, 0))) {
            measureTime = "점심"; // 점심 전/후 모두 포함
        } else if (currentTime.isAfter(LocalTime.of(17, 0)) && currentTime.isBefore(LocalTime.of(22, 0))) {
            measureTime = "저녁"; // 저녁 전/후 모두 포함
        } else {
            measureTime = "자기 전"; // 자기 전
        }

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(qMedicine)
                .leftJoin(qMedicine.intakes, qIntake)
                .where(qMedicine.user.id.eq(userId)
                        .and(qMedicine.create_At.eq(date))
                        .and(qIntake.intakeTime.any().contains(measureTime)) // 현재 시간대에 맞는 데이터 조회
                ).fetchOne()
        );
    }
}
