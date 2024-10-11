package com.springboot.peanut.data.dao.Impl;

import com.springboot.peanut.data.dao.PatientGuardianDao;
import com.springboot.peanut.data.entity.PatientGuardian;
import com.springboot.peanut.data.repository.PatientGuardianRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientGuardianDaoImpl implements PatientGuardianDao {

    private final PatientGuardianRepository patientGuardianRepository;

    @Override
    public void save(PatientGuardian patientGuardian) {
        patientGuardianRepository.save(patientGuardian);
    }
}
