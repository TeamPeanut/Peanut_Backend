package com.springboot.peanut.service.Impl;

import com.springboot.peanut.dto.mainPage.MainPageGetAdditionalInfoDto;
import com.springboot.peanut.dto.mainPage.MainPageGetUserDto;
import com.springboot.peanut.entity.BloodSugar;
import com.springboot.peanut.entity.Insulin;
import com.springboot.peanut.entity.Medicine;
import com.springboot.peanut.entity.User;
import com.springboot.peanut.jwt.JwtProvider;
import com.springboot.peanut.repository.BloodSugar.BloodSugarRepository;
import com.springboot.peanut.repository.Insulin.InsulinRepository;
import com.springboot.peanut.repository.Medicine.MedicineRepository;
import com.springboot.peanut.repository.UserRepository;
import com.springboot.peanut.service.MainPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MainPageServiceImpl implements MainPageService {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final BloodSugarRepository bloodSugarRepository;
    private final MedicineRepository medicineRepository;
    private final InsulinRepository insulinRepository;

    @Override
    public MainPageGetUserDto getUserInfoMainPage(HttpServletRequest request) {
        String token = jwtProvider.resolveToken(request);
        String email = jwtProvider.getUsername(token);
        log.info("[token] : {}", token);
        log.info("[email] : {}", email);
        User user = userRepository.getByEmail(email);
        log.info("[user] : {}", user);
        // 사용자 공복 혈당
        Optional<BloodSugar> fastingBloodSugar = bloodSugarRepository.findFastingBloodSugar(user.getId());
        String fastingBloodSugarLevel = fastingBloodSugar
                .map(BloodSugar::getBloodSugarLevel)
                .orElse("공복 혈당을 찾을 수 없습니다.");

        // 현재 시간과 가장 가까운 혈당
        Optional<BloodSugar> currentBloodSugarLevel = bloodSugarRepository.findClosestBloodSugar(user.getId());
        String currentBloodSugar = currentBloodSugarLevel
                .map(BloodSugar::getBloodSugarLevel)
                .orElse("최근에 등록된 혈당을 찾을 수 없습니다.");

        // 생성자로 객체 생성
        return new MainPageGetUserDto(
                user.getId(),
                user.getUserName(),
                user.getProfileUrl(),
                fastingBloodSugarLevel,
                currentBloodSugar
        );
    }

    @Override
    public MainPageGetAdditionalInfoDto getAdditionalInfoMainPage(HttpServletRequest request, LocalDate date) {
       String token = jwtProvider.resolveToken(request);
       String email = jwtProvider.getUsername(token);

       User user = userRepository.getByEmail(email);

       Optional<Medicine> medicine = medicineRepository.findByTodayMedicineInfo(user.getId(), date);
       Optional<Insulin> insulin = insulinRepository.findByTodayInsulinName(user.getId(),date);
       List<BloodSugar> bloodSugarList = bloodSugarRepository.findTodayBloodSugar(user.getId(),date);


       String medicineName = medicine.map(Medicine::getMedicineName).orElse("복용 기록 없음");
       Boolean medicineAlam = medicine.map(Medicine::isAlam).orElse(false);

       String insulinName = insulin.map(Insulin::getProductName).orElse("투여 기록 없음");
       Boolean insulinAlam = insulin.map(Insulin::isAlam).orElse(false);

        List<Map<String, LocalDateTime>> bloodSugarLevels = bloodSugarList.stream()
                .map(bloodSugar -> {
                    Map<String, LocalDateTime> map = new HashMap<>();
                    map.put(bloodSugar.getBloodSugarLevel(), bloodSugar.getCreate_At());
                    return map;
                })
                .collect(Collectors.toList());
        return new MainPageGetAdditionalInfoDto(
                bloodSugarLevels,
                medicineName,
                medicineAlam,
                insulinName,
                insulinAlam
        );
    }
}