package com.springboot.peanut.dao.Impl;

import com.springboot.peanut.dao.BloodSugarDao;
import com.springboot.peanut.entity.CurrentBloodSugar;
import com.springboot.peanut.entity.FastingBloodSugar;
import com.springboot.peanut.repository.CurrentBloodSugarRepository;
import com.springboot.peanut.repository.FastingBloodSugarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BloodSugarDaoImpl implements BloodSugarDao {

    private final FastingBloodSugarRepository fastingBloodSugarRepository;
    private final CurrentBloodSugarRepository currentBloodSugarRepository;

    @Override
    public void saveFastingBloodSugar(FastingBloodSugar fastingBloodSugar) {
        fastingBloodSugarRepository.save(fastingBloodSugar);
    }
    @Override
    public void saveCurrentBloodSugar(CurrentBloodSugar currentBloodSuger) {
        currentBloodSugarRepository.save(currentBloodSuger);
    }
}
