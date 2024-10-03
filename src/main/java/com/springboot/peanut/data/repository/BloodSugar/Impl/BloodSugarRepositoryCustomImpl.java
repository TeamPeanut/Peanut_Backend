package com.springboot.peanut.data.repository.BloodSugar.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.peanut.data.entity.BloodSugar;
import com.springboot.peanut.data.entity.QBloodSugar;
import com.springboot.peanut.data.repository.BloodSugar.BloodSugarRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class BloodSugarRepositoryCustomImpl implements BloodSugarRepositoryCustom {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    // 메인페이지 상단
    @Override
    public Optional<BloodSugar> findFastingBloodSugar(Long userId) {
        QBloodSugar bloodSugar = QBloodSugar.bloodSugar;

        LocalDate today = LocalDate.now();

        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(LocalTime.MAX);
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(bloodSugar)
                .where(bloodSugar.user.id.eq(userId)
                        .and(bloodSugar.measurementCondition.eq("공복"))
                        .and(bloodSugar.create_At.between(start,end)))
                .fetchOne());


    }

    // 메인페이지 상단
    @Override
    public Optional<BloodSugar> findClosestBloodSugar(Long userId) {
        QBloodSugar bloodSugar = QBloodSugar.bloodSugar;

        // 쿼리 작성: 현재 시간과 가장 가까운 혈당 기록을 찾기 위해 시간을 기준으로 정렬
        BloodSugar closestBloodSugar = jpaQueryFactory
                .selectFrom(bloodSugar)
                .where(bloodSugar.user.id.eq(userId))
                .orderBy(bloodSugar.create_At.desc())  // 시간을 오름차순으로 정렬
                .fetchFirst();  // 가장 가까운 하나의 혈당 기록만 가져오기

        return Optional.ofNullable(closestBloodSugar);
    }


    @Override
    public List<BloodSugar> findTodayBloodSugar(Long userId, LocalDate date) {
        QBloodSugar bloodSugar = QBloodSugar.bloodSugar;

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        return jpaQueryFactory
                .selectFrom(bloodSugar)
                .where(bloodSugar.user.id.eq(userId)
                        .and(bloodSugar.create_At.between(start,end)))
                .fetch();
    }

}
