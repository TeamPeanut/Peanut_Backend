package com.springboot.peanut.service.MainPage;

import com.springboot.peanut.data.entity.Insulin;
import com.springboot.peanut.data.entity.Medicine;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface MainPageTimeService {
    boolean getStatus(boolean currentStatus);

    List<Medicine> getMedicineListByTime(Long userId);

    Insulin getInsulinyTime(Long userId);

    String getInsulinTimeByCurrentTime(List<String> administrationTimes);


}
