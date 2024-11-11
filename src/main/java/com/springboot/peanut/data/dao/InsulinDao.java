package com.springboot.peanut.data.dao;

import com.springboot.peanut.data.entity.Insulin;

import java.util.List;

public interface InsulinDao {
    void saveInsulin(Insulin insulin);
    void  stopInsulin(Long insulId,Long userId, boolean activeStatus);
    Insulin getInsulinByUserId(Long userId);
    List<Insulin> findInsulinByYearAndMonth(Long userId, int year, int month) ;
    List<String> findAdministrationTimeByUserId(Long userId);
    List<Insulin> findInsulinByUserId(Long userId);
    }
