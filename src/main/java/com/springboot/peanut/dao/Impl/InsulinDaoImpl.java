package com.springboot.peanut.dao.Impl;

import com.springboot.peanut.dao.InsulinDao;
import com.springboot.peanut.entity.Insulin;
import com.springboot.peanut.repository.InsulinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InsulinDaoImpl implements InsulinDao {

    private final InsulinRepository insulinRepository;

    @Override
    public void saveInsulin(Insulin insulin) {
        insulinRepository.save(insulin);
    }
}
