package com.springboot.peanut.data.dao.Impl;

import com.springboot.peanut.data.dao.IntakeDao;
import com.springboot.peanut.data.entity.Intake;
import com.springboot.peanut.data.repository.IntakeRepository;
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
