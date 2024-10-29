package com.springboot.peanut.data.repository.Insulin;

import com.springboot.peanut.data.entity.Insulin;
import com.springboot.peanut.data.entity.Medicine;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InsulinRepositoryCustom {
  List<Insulin> findInsulinByYearAndMonth(Long userId, int year, int month);
  Optional<Insulin> findInsulinInfoByDate(Long userId, LocalDate date);
  Insulin findAllInsulinByUserId(Long userId);
    }
