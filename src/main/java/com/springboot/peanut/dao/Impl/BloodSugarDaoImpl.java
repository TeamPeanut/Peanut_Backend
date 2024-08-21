package com.springboot.peanut.dao.Impl;

import com.springboot.peanut.dao.BloodSugarDao;
import com.springboot.peanut.entity.BloodSugar;
import com.springboot.peanut.repository.BloodSugarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BloodSugarDaoImpl implements BloodSugarDao {

    private final BloodSugarRepository bloodSugarRepository;


    @Override
    public void saveBloodSugar(BloodSugar BloodSugar) {
        bloodSugarRepository.save(BloodSugar);
    }
}
