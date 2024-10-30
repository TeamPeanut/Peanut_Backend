package com.springboot.peanut.data.dao.Impl;

import com.springboot.peanut.data.dao.InsulinDao;
import com.springboot.peanut.data.entity.Insulin;
import com.springboot.peanut.data.repository.Insulin.InsulinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        Insulin insulin = insulinRepository.findByUserId(userId).orElse(null);
        return insulin;
    }

    @Override
    public List<Insulin> findInsulinByYearAndMonth(Long userId, int year, int month) {

        return insulinRepository.findInsulinByYearAndMonth(userId, year, month);
    }

    @Override
    public List<String> findAdministrationTimeByUserId(Long userId) {
        Optional<Insulin> optionalInsulin = insulinRepository.findByUserId(userId);

        if (optionalInsulin.isPresent()) {
            Insulin insulin = optionalInsulin.get();
            // administrationTime 리스트를 직접 반환
            return insulin.getAdministrationTime();
        } else {
            // 사용자가 존재하지 않을 경우 빈 리스트 반환
            return Collections.emptyList();
        }
    }

    @Override
    public List<Insulin> findInsulinByUserId(Long userId) {
        return List.of();
    }
}
