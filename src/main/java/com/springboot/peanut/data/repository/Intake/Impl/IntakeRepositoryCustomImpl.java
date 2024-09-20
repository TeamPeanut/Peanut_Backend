//package com.springboot.peanut.repository.Intake.Impl;
//
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import com.springboot.peanut.entity.Intake;
//import com.springboot.peanut.entity.QIntake;
//import com.springboot.peanut.repository.Intake.IntakeRepositoryCustom;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class IntakeRepositoryCustomImpl implements IntakeRepositoryCustom {
//
//    @Autowired
//    private JPAQueryFactory jpaQueryFactory;
//
//    @Override
//    public Optional<Intake> findByTodayIntakeStatus(Long userId, LocalDate date) {
//        QIntake intake = QIntake.intake;
//
//        return Optional.ofNullable(jpaQueryFactory
//                .selectFrom(intake)
//                .where(intake.user.id.eq(userId)
//                        .and(intake.create_At.eq(date)))
//                .fetchOne());
//    }
//}
