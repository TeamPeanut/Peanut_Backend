package com.springboot.peanut.service.MainPage.Impl;

import com.springboot.peanut.data.entity.Insulin;
import com.springboot.peanut.data.entity.InsulinRecord;
import com.springboot.peanut.data.entity.Medicine;
import com.springboot.peanut.data.entity.MedicineRecord;
import com.springboot.peanut.data.repository.Insulin.InsulinRepository;
import com.springboot.peanut.data.repository.InsulinRecord.InsulinRecordRepository;
import com.springboot.peanut.data.repository.Medicine.MedicineRepository;
import com.springboot.peanut.data.repository.MedicineRecord.MedicineRecordRepository;
import com.springboot.peanut.service.MainPage.MainPageTimeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MainPageTimeServiceImpl implements MainPageTimeService {
    private final MedicineRepository medicineRepository;
    private final InsulinRepository insulinRepository;
    private final InsulinRecordRepository insulinRecordRepository;
    private final MedicineRecordRepository medicineRecordRepository;

    @Override
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
    @Override
    public List<Medicine> getMedicineListByTime(Long userId) {
        // 현재 시간 확인
        LocalTime currentTime = LocalTime.now();
        List<Medicine> medicineList = medicineRepository.findByUserId(userId);

        // 시간대 정의
        LocalTime morningStart = LocalTime.of(6, 0);
        LocalTime morningEnd = LocalTime.of(11, 0);
        LocalTime lunchStart = LocalTime.of(11, 0);
        LocalTime lunchEnd = LocalTime.of(17, 0);
        LocalTime dinnerStart = LocalTime.of(17, 0);
        LocalTime dinnerEnd = LocalTime.of(22, 0);
        LocalTime sleepStart = LocalTime.of(22, 0);
        LocalTime sleepEnd = LocalTime.of(6, 0);

        // 현재 시간대에 맞는 복용 목록 반환
        if (currentTime.isAfter(morningStart) && currentTime.isBefore(morningEnd)) {
            // 아침 시간대
            return medicineList;  // 아침 약 목록 반환
        } else if (currentTime.isAfter(lunchStart) && currentTime.isBefore(lunchEnd)) {
            // 점심 시간대
            return medicineList;  // 점심 약 목록 반환
        } else if (currentTime.isAfter(dinnerStart) && currentTime.isBefore(dinnerEnd)) {
            // 저녁 시간대
            return medicineList;  // 저녁 약 목록 반환
        } else if (currentTime.isAfter(sleepStart) || currentTime.isBefore(sleepEnd)) {
            // 자기 전 시간대
            return medicineList;  // 자기 전 약 목록 반환
        }

        // 시간대에 맞는 목록이 없을 경우 기본값 반환
        return medicineList;
    }

    @Override
    public InsulinRecord getInsulinRecordTime(Long userId, LocalDate date) {
        // 현재 시간 확인
        Optional<InsulinRecord> insulinRecord = insulinRecordRepository.findInsulinRecordByUserIdAndDate(userId, date);

        if (insulinRecord.isEmpty()) {
            return null;  // 인슐린 기록이 없으면 null 반환
        }

        LocalTime currentTime = LocalTime.now();
        LocalTime recordTime = insulinRecord.get().getRecordTime();

        // 아침 시간대 (06:00 ~ 11:00)
        if (currentTime.isAfter(LocalTime.of(6, 0)) && currentTime.isBefore(LocalTime.of(11, 0))) {
            if (recordTime.isAfter(LocalTime.of(6, 0)) && recordTime.isBefore(LocalTime.of(11, 0))) {
                return insulinRecord.get();
            }
        }
        // 점심 시간대 (11:00 ~ 17:00)
        else if (currentTime.isAfter(LocalTime.of(11, 0)) && currentTime.isBefore(LocalTime.of(17, 0))) {
            if (recordTime.isAfter(LocalTime.of(11, 0)) && recordTime.isBefore(LocalTime.of(17, 0))) {
                return insulinRecord.get();
            }
        }
        // 저녁 시간대 (17:00 ~ 22:00)
        else if (currentTime.isAfter(LocalTime.of(17, 0)) && currentTime.isBefore(LocalTime.of(22, 0))) {
            if (recordTime.isAfter(LocalTime.of(17, 0)) && recordTime.isBefore(LocalTime.of(22, 0))) {
                return insulinRecord.get();
            }
        }
        // 밤 시간대 (22:00 ~ 06:00)
        else {
            if ((recordTime.isAfter(LocalTime.of(22, 0)) && recordTime.isBefore(LocalTime.MIDNIGHT)) ||
                    (recordTime.isAfter(LocalTime.MIDNIGHT) && recordTime.isBefore(LocalTime.of(6, 0)))) {
                return insulinRecord.get();
            }
        }

        return null;  // 해당 시간대에 기록이 없을 경우
    }
    @Override
    public Optional<MedicineRecord> getMedicineRecordByTime(Long userId, LocalDate date) {
        // 현재 시간에 맞는 MedicineRecord 리스트 가져오기
        Optional<List<MedicineRecord>> medicineRecords = medicineRecordRepository.findMedicineRecordByUserId(userId, date);

        if (medicineRecords.isEmpty()) {
            return Optional.empty();  // 기록이 없으면 빈 값 반환
        }

        LocalTime currentTime = LocalTime.now();

        // 시간대 정의
        LocalTime morningStart = LocalTime.of(6, 0);
        LocalTime morningEnd = LocalTime.of(11, 0);
        LocalTime lunchStart = LocalTime.of(11, 0);
        LocalTime lunchEnd = LocalTime.of(17, 0);
        LocalTime dinnerStart = LocalTime.of(17, 0);
        LocalTime dinnerEnd = LocalTime.of(22, 0);
        LocalTime sleepStart = LocalTime.of(22, 0);
        LocalTime sleepEnd = LocalTime.of(6, 0);

        // 리스트에서 해당 시간대에 맞는 첫 번째 MedicineRecord를 찾음
        for (MedicineRecord record : medicineRecords.get()) {
            LocalTime recordTime = record.getRecordTime();

            // 아침 시간대
            if (currentTime.isAfter(morningStart) && currentTime.isBefore(morningEnd)) {
                if (recordTime.isAfter(morningStart) && recordTime.isBefore(morningEnd)) {
                    return Optional.of(record);
                }
            }
            // 점심 시간대
            else if (currentTime.isAfter(lunchStart) && currentTime.isBefore(lunchEnd)) {
                if (recordTime.isAfter(lunchStart) && recordTime.isBefore(lunchEnd)) {
                    return Optional.of(record);
                }
            }
            // 저녁 시간대
            else if (currentTime.isAfter(dinnerStart) && currentTime.isBefore(dinnerEnd)) {
                if (recordTime.isAfter(dinnerStart) && recordTime.isBefore(dinnerEnd)) {
                    return Optional.of(record);
                }
            }
            // 자기 전 시간대 (22:00 ~ 06:00)
            else if (currentTime.isAfter(sleepStart) || currentTime.isBefore(sleepEnd)) {
                if (recordTime.isAfter(sleepStart) || recordTime.isBefore(sleepEnd)) {
                    return Optional.of(record);
                }
            }
        }

        return Optional.empty(); // 시간대에 맞는 기록이 없을 경우 빈 값 반환
    }


    @Override
    public String getInsulinTimeByCurrentTime(List<String> administrationTimes) {
        if (administrationTimes == null || administrationTimes.isEmpty()) {
            return "투여 기록 없음";  // 리스트가 비어 있을 경우 기본 메시지
        }

        LocalTime currentTime = LocalTime.now();

        // 시간대 정의
        LocalTime morningStart = LocalTime.of(6, 0);
        LocalTime morningEnd = LocalTime.of(11, 0);
        LocalTime lunchStart = LocalTime.of(11, 0);
        LocalTime lunchEnd = LocalTime.of(17, 0);
        LocalTime dinnerStart = LocalTime.of(17, 0);
        LocalTime dinnerEnd = LocalTime.of(22, 0);
        LocalTime sleepStart = LocalTime.of(22, 0);
        LocalTime sleepEnd = LocalTime.of(6, 0);

        // 현재 시간대에 맞는 복용 상태 찾기
        for (String time : administrationTimes) {
            // 아침 시간대 (06:00 ~ 11:00)
            if (currentTime.isAfter(morningStart) && currentTime.isBefore(morningEnd)) {
                if (time.contains("아침 전")) {
                    return "아침 전";
                } else if (time.contains("아침 후")) {
                    return "아침 후";
                }
            }
            // 점심 시간대 (11:00 ~ 17:00)
            else if (currentTime.isAfter(lunchStart) && currentTime.isBefore(lunchEnd)) {
                if (time.contains("점심 전")) {
                    return "점심 전";
                } else if (time.contains("점심 후")) {
                    return "점심 후";
                }
            }
            // 저녁 시간대 (17:00 ~ 22:00)
            else if (currentTime.isAfter(dinnerStart) && currentTime.isBefore(dinnerEnd)) {
                if (time.contains("저녁 전")) {
                    return "저녁 전";
                } else if (time.contains("저녁 후")) {
                    return "저녁 후";
                }
            }
            // 밤 시간대 (22:00 ~ 06:00)
            else if (currentTime.isAfter(sleepStart) || currentTime.isBefore(sleepEnd)) {
                if (time.contains("자기 전")) {
                    return "자기 전";
                }
            }
        }

        return "투여 기록 없음";  // 해당 시간대가 없으면 기본 메시지 반환
    }
    @Override
    public String getIntakeTimeByCurrentTime(List<String> intakeTimes) {
        if (intakeTimes == null || intakeTimes.isEmpty()) {
            return "복용 기록 없음";  // 복약 시간이 없을 경우 기본 메시지
        }

        LocalTime currentTime = LocalTime.now();

        // 시간대 정의
        LocalTime morningStart = LocalTime.of(6, 0);
        LocalTime morningEnd = LocalTime.of(11, 0);
        LocalTime lunchStart = LocalTime.of(11, 0);
        LocalTime lunchEnd = LocalTime.of(17, 0);
        LocalTime dinnerStart = LocalTime.of(17, 0);
        LocalTime dinnerEnd = LocalTime.of(22, 0);
        LocalTime sleepStart = LocalTime.of(22, 0);
        LocalTime sleepEnd = LocalTime.of(6, 0);

        // 현재 시간대에 맞는 복약 시간 찾기
        for (String time : intakeTimes) {
            // 아침 시간대 (06:00 ~ 11:00)
            if (currentTime.isAfter(morningStart) && currentTime.isBefore(morningEnd)) {
                if (time.contains("아침 전")) {
                    return "아침 전";
                } else if (time.contains("아침 후")) {
                    return "아침 후";
                }
            }
            // 점심 시간대 (11:00 ~ 17:00)
            else if (currentTime.isAfter(lunchStart) && currentTime.isBefore(lunchEnd)) {
                if (time.contains("점심 전")) {
                    return "점심 전";
                } else if (time.contains("점심 후")) {
                    return "점심 후";
                }
            }
            // 저녁 시간대 (17:00 ~ 22:00)
            else if (currentTime.isAfter(dinnerStart) && currentTime.isBefore(dinnerEnd)) {
                if (time.contains("저녁 전")) {
                    return "저녁 전";
                } else if (time.contains("저녁 후")) {
                    return "저녁 후";
                }
            }
            // 자기 전 시간대 (22:00 ~ 06:00)
            else if (currentTime.isAfter(sleepStart) || currentTime.isBefore(sleepEnd)) {
                if (time.contains("자기 전")) {
                    return "자기 전";
                }
            }
        }

        return "복용 기록 없음";  // 해당 시간대에 맞는 복약 시간이 없을 경우
    }

}
