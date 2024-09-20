package com.springboot.peanut.data.dao.Impl;

import com.springboot.peanut.data.dao.BloodSugarDao;
import com.springboot.peanut.data.entity.BloodSugar;
import com.springboot.peanut.data.repository.BloodSugar.BloodSugarRepository;
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
