package com.springboot.peanut.data.repository.Insulin.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.peanut.data.entity.Insulin;
import com.springboot.peanut.data.entity.QInsulin;
import com.springboot.peanut.data.entity.QUser;
import com.springboot.peanut.data.repository.Insulin.InsulinRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InsulinRepositoryCustomImpl implements InsulinRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Insulin> findInsulinInfoByDate(Long userId, LocalDate date) {
        QInsulin qInsulin = QInsulin.insulin;
        QUser qUser = QUser.user;
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
                .leftJoin(qInsulin.user, qUser).fetchJoin() // Fetch Join 추가
                .where(qInsulin.user.id.eq(userId)
                        .and(qInsulin.administrationTime.any().contains(administrationTime)))
                .fetchOne());
    }

    @Override
    public List<Insulin> findInsulinByYearAndMonth(Long userId, int year, int month) {
        QInsulin qInsulin = QInsulin.insulin;
        QUser qUser = QUser.user;

        return jpaQueryFactory.selectFrom(qInsulin)
                .leftJoin(qInsulin.user, qUser).fetchJoin() // Fetch Join 추가
                .where(qInsulin.user.id.eq(userId)
                        .and(qInsulin.create_At.year().eq(year))
                        .and(qInsulin.create_At.month().eq(month)))
                .fetch();
    }

    @Override
    public Insulin findAllInsulinByUserId(Long userId) {
        QInsulin qInsulin = QInsulin.insulin;
        QUser qUser = QUser.user;

        return jpaQueryFactory.selectFrom(qInsulin)
                .leftJoin(qInsulin.user, qUser).fetchJoin() // Fetch Join 추가
                .where(qInsulin.user.id.eq(userId))
                .fetchOne();
    }

    @Override
    public Optional<Insulin> findInsulinByIdAndUserId(Long id, Long userId) {
        QInsulin qInsulin = QInsulin.insulin;
        QUser qUser = QUser.user;

        return Optional.ofNullable(jpaQueryFactory.selectFrom(qInsulin)
                .leftJoin(qInsulin.user, qUser).fetchJoin() // Fetch Join 추가
                .where(qInsulin.user.id.eq(userId)
                        .and(qInsulin.id.eq(id)))
                .fetchOne());
    }
}