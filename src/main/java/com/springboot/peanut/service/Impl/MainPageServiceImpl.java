package com.springboot.peanut.service.Impl;

import com.springboot.peanut.dto.mainPage.MainPageGetUserDto;
import com.springboot.peanut.entity.BloodSugar;
import com.springboot.peanut.entity.User;
import com.springboot.peanut.jwt.JwtProvider;
import com.springboot.peanut.repository.BloodSugar.BloodSugarRepository;
import com.springboot.peanut.repository.UserRepository;
import com.springboot.peanut.service.MainPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MainPageServiceImpl implements MainPageService {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final BloodSugarRepository bloodSugarRepository;

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
                user.getUsername(),
                user.getProfileUrl(),
                fastingBloodSugarLevel,
                currentBloodSugar
        );
    }

}