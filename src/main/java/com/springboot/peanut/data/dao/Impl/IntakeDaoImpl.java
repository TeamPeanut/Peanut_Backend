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
        Optional<Intake> optionalIntake = intakeRepository.findByUserId(userId);

        if (optionalIntake.isPresent()) {
            Intake intake = optionalIntake.get();
            // administrationTime 리스트를 직접 반환
            return intake.getIntakeTime();
        } else {
            // 사용자가 존재하지 않을 경우 빈 리스트 반환
            return Collections.emptyList();
        }

    }
}
