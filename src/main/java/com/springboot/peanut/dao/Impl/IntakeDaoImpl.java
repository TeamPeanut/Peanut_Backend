package com.springboot.peanut.dao.Impl;

import com.springboot.peanut.dao.IntakeDao;
import com.springboot.peanut.dao.MedicineDao;
import com.springboot.peanut.entity.Intake;
import com.springboot.peanut.entity.Medicine;
import com.springboot.peanut.repository.IntakeRepository;
import com.springboot.peanut.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IntakeDaoImpl implements IntakeDao {

    private final IntakeRepository intakeRepository;

    @Override
    public void saveIntakeInfo(Intake intake) {
        intakeRepository.save(intake);
    }
}
