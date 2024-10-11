package com.springboot.peanut.data.repository.Insulin;

import com.springboot.peanut.data.entity.Insulin;
import com.springboot.peanut.data.entity.Medicine;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;

public interface InsulinRepositoryCustom {
    Optional<Insulin> findByTodayInsulinName(Long userId, LocalDate date);
    Optional<Insulin> findInsulinInfoByDate(Long userId, LocalDate date);
    }
