package com.springboot.peanut.data.dao.Impl;

import com.springboot.peanut.data.dao.IntakeDao;
import com.springboot.peanut.data.entity.Insulin;
import com.springboot.peanut.data.entity.Intake;
import com.springboot.peanut.data.repository.Intake.IntakeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IntakeDaoImpl implements IntakeDao {

    private final IntakeRepository intakeRepository;

    @Override
    public void saveIntakeInfo(Intake intake) {
        intakeRepository.save(intake);
    }

    @Override
    public List<String> findIntakeTime(Long userId) {
        List<Intake> intakeList = intakeRepository.findAllByUserId(userId);

        return intakeList.stream()
                .flatMap(intake -> intake.getIntakeTime().stream())
                .collect(Collectors.toList());
    }
}
