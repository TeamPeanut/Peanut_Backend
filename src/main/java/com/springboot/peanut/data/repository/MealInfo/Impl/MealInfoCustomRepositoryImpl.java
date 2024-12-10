package com.springboot.peanut.data.repository.MealInfo.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.peanut.data.entity.MealInfo;
import com.springboot.peanut.data.entity.QMealInfo;
import com.springboot.peanut.data.entity.QUser;
import com.springboot.peanut.data.repository.MealInfo.MealInfoCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MealInfoCustomRepositoryImpl implements MealInfoCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<List<MealInfo>> getByUserAllMealInfo(LocalDate date, Long userId) {
        QMealInfo qMealInfo = QMealInfo.mealInfo;
        QUser qUser = QUser.user;

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(qMealInfo)
                .leftJoin(qMealInfo.user, qUser).fetchJoin() // Fetch Join 추가
                .where(qMealInfo.user.id.eq(userId)
                        .and(qMealInfo.create_At.eq(date)))
                .fetch());
    }

    @Override
    public Optional<MealInfo> getMealInfoByEatTime(LocalDate date, Long userId, String eatTime) {
        QMealInfo qMealInfo = QMealInfo.mealInfo;
        QUser qUser = QUser.user;

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(qMealInfo)
                .leftJoin(qMealInfo.user, qUser).fetchJoin() // Fetch Join 추가
                .where(qMealInfo.user.id.eq(userId)
                        .and(qMealInfo.create_At.eq(date))
                        .and(qMealInfo.eatTime.eq(eatTime)))
                .fetchOne());
    }

    @Override
    public Optional<List<MealInfo>> getMealInfoListByEatTime(LocalDate date, Long userId, String eatTime) {
        QMealInfo qMealInfo = QMealInfo.mealInfo;
        QUser qUser = QUser.user; // QUser 추가

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(qMealInfo)
                .leftJoin(qMealInfo.user, qUser).fetchJoin() // Fetch Join 추가
                .where(qMealInfo.user.id.eq(userId)
                        .and(qMealInfo.create_At.eq(date))
                        .and(qMealInfo.eatTime.eq(eatTime)))
                .fetch());
    }
}