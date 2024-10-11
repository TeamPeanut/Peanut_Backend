package com.springboot.peanut.data.repository.Intake;


import com.springboot.peanut.data.entity.Intake;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;

public interface IntakeRepositoryCustom {
    Optional<Intake> findByTodayIntakeStatus(Long userId, LocalDate date );
}
