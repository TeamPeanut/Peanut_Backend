package com.springboot.peanut.repository.MealInfo.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.peanut.entity.MealInfo;
import com.springboot.peanut.entity.QMealInfo;
import com.springboot.peanut.repository.MealInfo.MealInfoCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class MealInfoCustomRepositoryImpl implements MealInfoCustomRepository {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<List<MealInfo>> getByUserAllMealInfo(LocalDate date, Long userId) {
        QMealInfo qMealInfo = QMealInfo.mealInfo;

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(qMealInfo)
                .where(qMealInfo.user.id.eq(userId)
                        .and(qMealInfo.create_At.eq(date)))
                .fetch());
    }

    @Override
    public Optional<MealInfo> getMealInfoByEatTime(LocalDate date, Long userId, String eatTime) {
        QMealInfo qMealInfo = QMealInfo.mealInfo;

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(qMealInfo)
                .where(qMealInfo.user.id.eq(userId)
                        .and(qMealInfo.create_At.eq(date))
                        .and(qMealInfo.eatTime.eq(eatTime)))
                .fetchOne());
    }
}
