package com.springboot.peanut.service.Impl;

import com.springboot.peanut.dao.BloodSugarDao;
import com.springboot.peanut.dto.bloodSugar.BloodSugarRequestDto;
import com.springboot.peanut.dto.bloodSugar.BloodSugarResponseDto;
import com.springboot.peanut.entity.BloodSugar;
import com.springboot.peanut.entity.User;
import com.springboot.peanut.jwt.JwtProvider;
import com.springboot.peanut.repository.UserRepository;
import com.springboot.peanut.service.BloodSugarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BloodSugarServiceImpl implements BloodSugarService {
    private final BloodSugarDao bloodSugarDao;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;


    @Override
    public BloodSugarResponseDto saveBloodSugar(BloodSugarRequestDto bloodSugarRequestDto, HttpServletRequest request) {
        String info = jwtProvider.getUsername(request.getHeader("X-AUTH-TOKEN"));
        User user = userRepository.getByEmail(info);

        BloodSugar bloodSugar = new BloodSugar();
        bloodSugar.setBloodSugarLevel(bloodSugarRequestDto.getBlood_sugar());
        bloodSugar.setMeasurementTime(bloodSugarRequestDto.getMeasurementTime());
        bloodSugar.setMemo(bloodSugarRequestDto.getMemo());
        bloodSugar.setCreate_At(LocalDateTime.now());
        bloodSugar.setUser(user);

        bloodSugarDao.saveBloodSugar(bloodSugar);

        BloodSugarResponseDto bloodSugarResponseDto = new BloodSugarResponseDto();

        bloodSugarResponseDto.setId(bloodSugar.getId());
        bloodSugarResponseDto.setCurrent_blood_sugar(bloodSugar.getBloodSugarLevel());
        bloodSugarResponseDto.setMeasurementTime(bloodSugar.getMeasurementTime());
        bloodSugarResponseDto.setMemo(bloodSugar.getMemo());


        return bloodSugarResponseDto;
    }
}
