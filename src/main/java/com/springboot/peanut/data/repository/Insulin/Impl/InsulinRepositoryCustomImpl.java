package com.springboot.peanut.data.repository.Insulin.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.peanut.data.entity.Insulin;
import com.springboot.peanut.data.entity.QInsulin;
import com.springboot.peanut.data.repository.Insulin.InsulinRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class InsulinRepositoryCustomImpl implements InsulinRepositoryCustom {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Insulin> findByTodayInsulinName(Long userId, LocalDate date) {
        QInsulin qInsulin = QInsulin.insulin;

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(qInsulin)
                .where(qInsulin.user.id.eq(userId)
                        .and(qInsulin.create_At.eq(date)))
                .fetchOne());
    }

    @Override
    public Optional<Insulin> findInsulinInfoByDate(Long userId, LocalDate date) {
       QInsulin qInsulin = QInsulin.insulin;
        LocalTime currentTime = LocalTime.now();
        String administrationTime;

        // 시간대에 따라 measureTime 설정
        if (currentTime.isAfter(LocalTime.of(6, 0)) && currentTime.isBefore(LocalTime.of(11, 0))) {
            administrationTime = "아침"; // 아침 전/후 모두 포함
        } else if (currentTime.isAfter(LocalTime.of(11, 0)) && currentTime.isBefore(LocalTime.of(17, 0))) {
            administrationTime = "점심"; // 점심 전/후 모두 포함
        } else if (currentTime.isAfter(LocalTime.of(17, 0)) && currentTime.isBefore(LocalTime.of(22, 0))) {
            administrationTime = "저녁"; // 저녁 전/후 모두 포함
        } else {
            administrationTime = "자기 전"; // 자기 전
        }
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(qInsulin)
                .where(qInsulin.user.id.eq(userId)
                        .and(qInsulin.administrationTime.any().contains(administrationTime))
                )
                .fetchOne());
    }
}
