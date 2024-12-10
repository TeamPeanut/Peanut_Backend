package com.springboot.peanut.data.repository.BloodSugar.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.peanut.data.entity.BloodSugar;
import com.springboot.peanut.data.entity.QBloodSugar;
import com.springboot.peanut.data.entity.QUser;
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
        QUser user = QUser.user; // QUser 추가

        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(LocalTime.MAX);

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(bloodSugar)
                .leftJoin(bloodSugar.user, user).fetchJoin() // Fetch Join 추가
                .where(bloodSugar.user.id.eq(userId)
                        .and(bloodSugar.measurementCondition.eq("공복"))
                        .and(bloodSugar.create_At.between(start, end)))
                .fetchOne());
    }

    @Override
    public Optional<BloodSugar> findClosestBloodSugar(Long userId) {
        QBloodSugar bloodSugar = QBloodSugar.bloodSugar;
        QUser user = QUser.user; // QUser 추가

        BloodSugar closestBloodSugar = jpaQueryFactory
                .selectFrom(bloodSugar)
                .leftJoin(bloodSugar.user, user).fetchJoin() // Fetch Join 추가
                .where(bloodSugar.user.id.eq(userId))
                .orderBy(bloodSugar.create_At.desc()) // 가장 최근 데이터 기준
                .fetchFirst(); // 첫 번째 데이터만 가져오기

        return Optional.ofNullable(closestBloodSugar);
    }

    @Override
    public List<BloodSugar> findTodayBloodSugar(Long userId, LocalDate date) {
        QBloodSugar bloodSugar = QBloodSugar.bloodSugar;
        QUser user = QUser.user;

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        return jpaQueryFactory
                .selectFrom(bloodSugar)
                .leftJoin(bloodSugar.user, user).fetchJoin() // Fetch Join 추가
                .where(bloodSugar.user.id.eq(userId)
                        .and(bloodSugar.create_At.between(start, end)))
                .fetch();
    }

    @Override
    public List<BloodSugar> findByUserAndMonth(Long userId, int year, int month) {
        QBloodSugar bloodSugar = QBloodSugar.bloodSugar;
        QUser user = QUser.user;

        return jpaQueryFactory
                .selectFrom(bloodSugar)
                .leftJoin(bloodSugar.user, user).fetchJoin() // Fetch Join 추가
                .where(bloodSugar.user.id.eq(userId)
                        .and(bloodSugar.measurementTime.month().eq(month))
                        .and(bloodSugar.measurementTime.year().eq(year)))
                .fetch();
    }
}