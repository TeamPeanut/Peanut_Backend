package com.springboot.peanut.data.repository.Insulin;

import com.springboot.peanut.data.entity.Insulin;

import java.time.LocalDate;
import java.util.Optional;

public interface InsulinRepositoryCustom {
    Optional<Insulin> findByTodayInsulinName(Long userId, LocalDate date);
}
