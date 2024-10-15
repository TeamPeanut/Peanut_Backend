package com.springboot.peanut.data.dao;

import com.springboot.peanut.data.entity.Insulin;

import java.util.List;

public interface InsulinDao {
    void saveInsulin(Insulin insulin);
    Insulin getInsulinByUserId(Long userId);
    List<Insulin> findInsulinByYearAndMonth(Long userId, int year, int month) ;

    }
