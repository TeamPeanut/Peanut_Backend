package com.springboot.peanut.data.dao.Impl;

import com.springboot.peanut.data.dao.InsulinDao;
import com.springboot.peanut.data.entity.Insulin;
import com.springboot.peanut.data.repository.Insulin.InsulinRepository;
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

    @Override
    public Insulin getInsulinByUserId(Long userId) {
        Insulin insulin = insulinRepository.findByUserId(userId).get();
        return insulin;
    }
}
