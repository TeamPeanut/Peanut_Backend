package com.springboot.peanut.data.repository.Intake.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;


import com.springboot.peanut.data.entity.Intake;
import com.springboot.peanut.data.entity.QIntake;
import com.springboot.peanut.data.repository.Intake.IntakeRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IntakeRepositoryCustomImpl implements IntakeRepositoryCustom {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Intake> findByTodayIntakeStatus(Long userId, LocalDate date) {
        QIntake qIntake = QIntake.intake;
        LocalTime currentTime = LocalTime.now();
        String measureTime;

        // 시간대에 따라 measureTime 설정
        if (currentTime.isAfter(LocalTime.of(6, 0)) && currentTime.isBefore(LocalTime.of(11, 0))) {
            measureTime = "아침";
        } else if (currentTime.isAfter(LocalTime.of(11, 0)) && currentTime.isBefore(LocalTime.of(17, 0))) {
            measureTime = "점심";
        } else if (currentTime.isAfter(LocalTime.of(17, 0)) && currentTime.isBefore(LocalTime.of(22, 0))) {
            measureTime = "저녁";
        } else {
            measureTime = "자기 전";
        }

        Optional<Intake> intake = Optional.ofNullable(jpaQueryFactory
                .selectFrom(qIntake)
                .where(qIntake.user.id.eq(userId)
                        .and(qIntake.intakeTime.any().contains(measureTime)) // 해당 시간대에 맞는 값 필터링
                ).fetchOne());

        // 특정 시간대에 맞는 intakeTime을 가져온 경우, 리스트에서 하나만 반환
        return intake.map(i -> {
            i.setIntakeTime(i.getIntakeTime().stream()
                    .filter(time -> time.contains(measureTime))
                    .collect(Collectors.toList()));
            return i;
        });
    }
}