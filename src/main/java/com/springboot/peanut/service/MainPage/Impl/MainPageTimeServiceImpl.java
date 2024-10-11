package com.springboot.peanut.service.MainPage.Impl;

import com.springboot.peanut.data.entity.Insulin;
import com.springboot.peanut.data.entity.Medicine;
import com.springboot.peanut.data.repository.Insulin.InsulinRepository;
import com.springboot.peanut.data.repository.Medicine.MedicineRepository;
import com.springboot.peanut.service.MainPage.MainPageTimeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MainPageTimeServiceImpl implements MainPageTimeService {
    private final MedicineRepository medicineRepository;
    private final InsulinRepository insulinRepository;

    public boolean getStatus(boolean currentStatus) {
        // 현재 시간 확인
        LocalTime currentTime = LocalTime.now();
        if (currentStatus) {
            return true;
        }
        // 아침, 점심, 저녁 시간대에 따라 상태 초기화
        if (currentTime.isAfter(LocalTime.of(6, 0)) && currentTime.isBefore(LocalTime.of(11, 0))) {
            // 아침 시간대
            return false;
        } else if (currentTime.isAfter(LocalTime.of(11, 0)) && currentTime.isBefore(LocalTime.of(17, 0))) {
            // 점심 시간대
            return false; // 점심 시간에는 초기화
        } else if (currentTime.isAfter(LocalTime.of(17, 0)) && currentTime.isBefore(LocalTime.of(22, 0))) {
            // 저녁 시간대
            return false; // 저녁 시간에는 초기화
        } else {
            // 자기 전 시간대
            return false; // 자기 전 시간에는 초기화
        }
    }

    public List<Medicine> getMedicineListByTime(Long userId) {
        // 현재 시간 확인
        LocalTime currentTime = LocalTime.now();
        List<Medicine> medicineList = medicineRepository.findByUserId(userId);

        if (currentTime.isAfter(LocalTime.of(6, 0)) && currentTime.isBefore(LocalTime.of(11, 0))) {
            // 아침 시간대
            return medicineList;
        } else if (currentTime.isAfter(LocalTime.of(11, 0)) && currentTime.isBefore(LocalTime.of(17, 0))) {
            // 점심 시간대
            return medicineList;
        } else if (currentTime.isAfter(LocalTime.of(17, 0)) && currentTime.isBefore(LocalTime.of(22, 0))) {
            // 저녁 시간대
            return medicineList;
        } else {

            return medicineList;
        }
    }

    public Insulin getInsulinyTime(Long userId) {
        // 현재 시간 확인
        LocalTime currentTime = LocalTime.now();
        Optional<Insulin> insulin = insulinRepository.findByUserId(userId);

        if (currentTime.isAfter(LocalTime.of(6, 0)) && currentTime.isBefore(LocalTime.of(11, 0))) {
            // 아침 시간대
            return insulin.get();
        } else if (currentTime.isAfter(LocalTime.of(11, 0)) && currentTime.isBefore(LocalTime.of(17, 0))) {
            // 점심 시간대
            return insulin.get();
        } else if (currentTime.isAfter(LocalTime.of(17, 0)) && currentTime.isBefore(LocalTime.of(22, 0))) {
            // 저녁 시간대
            return insulin.get();
        } else {

            return insulin.get();
        }
    }

    public String getInsulinTimeByCurrentTime(List<String> administrationTimes) {
        if (administrationTimes == null || administrationTimes.isEmpty()) {
            return "투여 기록 없음";  // 리스트가 비어 있을 경우 기본 메시지
        }

        LocalTime currentTime = LocalTime.now();

        // 현재 시간과 가장 가까운 시간을 찾음
        for (String time : administrationTimes) {
            if (currentTime.isAfter(LocalTime.of(6, 0)) && currentTime.isBefore(LocalTime.of(11, 0))) {
                return time; // 아침 시간대에 맞는 시간 반환
            } else if (currentTime.isAfter(LocalTime.of(11, 0)) && currentTime.isBefore(LocalTime.of(17, 0))) {
                return time;  // 점심 시간대에 맞는 시간 반환
            } else if (currentTime.isAfter(LocalTime.of(17, 0)) && currentTime.isBefore(LocalTime.of(22, 0))) {
                return time;  // 저녁 시간대에 맞는 시간 반환
            }
        }

        return "투여 기록 없음";  // 해당 시간대가 없으면 기본 메시지 반환
    }


}
