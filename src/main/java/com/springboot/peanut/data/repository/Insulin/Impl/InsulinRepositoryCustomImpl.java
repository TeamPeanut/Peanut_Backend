package com.springboot.peanut.data.repository.Insulin.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.peanut.data.entity.Insulin;
import com.springboot.peanut.entity.QInsulin;
import com.springboot.peanut.data.repository.Insulin.InsulinRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
}
