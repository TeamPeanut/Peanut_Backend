package com.springboot.peanut.service.MainPage;

import com.springboot.peanut.data.entity.Insulin;
import com.springboot.peanut.data.entity.InsulinRecord;
import com.springboot.peanut.data.entity.Medicine;
import com.springboot.peanut.data.entity.MedicineRecord;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface MainPageTimeService {
    boolean getStatus(boolean currentStatus);

    List<Medicine> getMedicineListByTime(Long userId);

    String getIntakeTimeByCurrentTime(List<String> intakeTimes);
    InsulinRecord getInsulinRecordTime(Long userId, LocalDate date);
    Optional<MedicineRecord> getMedicineRecordByTime(Long userId, LocalDate date);
    String getInsulinTimeByCurrentTime(List<String> administrationTimes);
    }
