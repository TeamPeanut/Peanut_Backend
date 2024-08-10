package com.springboot.peanut.service.Impl;

import com.springboot.peanut.dao.BloodSugarDao;
import com.springboot.peanut.dto.BloodSugarDto.CurrentResponseDto;
import com.springboot.peanut.dto.BloodSugarDto.FastingResponseDto;
import com.springboot.peanut.entity.CurrentBloodSugar;
import com.springboot.peanut.entity.FastingBloodSugar;
import com.springboot.peanut.entity.User;
import com.springboot.peanut.jwt.JwtProvider;
import com.springboot.peanut.repository.UserRepository;
import com.springboot.peanut.service.BloodSugarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BloodSugarServiceImpl implements BloodSugarService {
    private final BloodSugarDao bloodSugarDao;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    public FastingResponseDto saveFastingBloodSugar(int fasting_blood_sugar, HttpServletRequest request) {
        String info = jwtProvider.getUsername(request.getHeader("X-AUTH-TOKEN"));
        User user = userRepository.getByEmail(info);

        FastingBloodSugar fastingBloodSugar = new FastingBloodSugar();
        fastingBloodSugar.setFasting_SugarInfo(fasting_blood_sugar);
        fastingBloodSugar.setRecord_time(LocalDate.now());
        fastingBloodSugar.setUser(user);

        bloodSugarDao.saveFastingBloodSugar(fastingBloodSugar);

        FastingResponseDto fastingResponseDto = new FastingResponseDto();

        fastingResponseDto.setId(fastingBloodSugar.getId());
        fastingResponseDto.setFasting_blood_sugar(fastingBloodSugar.getFasting_SugarInfo());
        fastingResponseDto.setRecord_time(LocalDate.now());

        return fastingResponseDto;
    }

    @Override
    public CurrentResponseDto saveCurrentBloodSugar(int current_blood_sugar, HttpServletRequest request) {
        String info = jwtProvider.getUsername(request.getHeader("X-AUTH-TOKEN"));
        User user = userRepository.getByEmail(info);

        CurrentBloodSugar currentBloodSugar = new CurrentBloodSugar();
        currentBloodSugar.setCurrent_blood_sugar(current_blood_sugar);
        currentBloodSugar.setRecord_time(LocalDate.now());
        currentBloodSugar.setUser(user);

        bloodSugarDao.saveCurrentBloodSugar(currentBloodSugar);

        CurrentResponseDto currentResponseDto = new CurrentResponseDto();

        currentResponseDto.setId(currentBloodSugar.getId());
        currentResponseDto.setCurrent_blood_sugar(currentBloodSugar.getCurrent_blood_sugar());
        currentResponseDto.setRecord_time(LocalDate.now());

        return currentResponseDto;
    }
}
