package com.springboot.peanut.repository.Insulin;

import com.springboot.peanut.entity.Insulin;

import java.time.LocalDate;
import java.util.Optional;

public interface InsulinRepositoryCustom {
    Optional<Insulin> findByTodayInsulinName(Long userId, LocalDate date);
}
