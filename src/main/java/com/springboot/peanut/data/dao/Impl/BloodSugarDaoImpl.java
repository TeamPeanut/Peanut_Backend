package com.springboot.peanut.data.dao.Impl;

import com.springboot.peanut.data.dao.BloodSugarDao;
import com.springboot.peanut.data.entity.BloodSugar;
import com.springboot.peanut.data.repository.BloodSugar.BloodSugarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BloodSugarDaoImpl implements BloodSugarDao {

    private final BloodSugarRepository bloodSugarRepository;


    @Override
    public void saveBloodSugar(BloodSugar BloodSugar) {
        bloodSugarRepository.save(BloodSugar);
    }

    @Override
    public List<BloodSugar> findByUserAndMonth(Long userId, int year, int month) {
        return bloodSugarRepository.findByUserAndMonth(userId, year, month);
    }

    @Override
    public Optional<BloodSugar> findFastingBloodSugar(Long userId) {
        return bloodSugarRepository.findClosestBloodSugar(userId);
    }

    @Override
    public Optional<BloodSugar> findClosestBloodSugar(Long userId) {
        return bloodSugarRepository.findClosestBloodSugar(userId);
    }

    @Override
    public List<BloodSugar> findTodayBloodSugar(Long id, LocalDate date) {
        return bloodSugarRepository.findTodayBloodSugar(id, date);
    }
}
