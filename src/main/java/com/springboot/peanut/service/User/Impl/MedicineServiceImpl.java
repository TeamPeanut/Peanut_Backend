package com.springboot.peanut.service.User.Impl;

import com.springboot.peanut.data.dao.IntakeDao;
import com.springboot.peanut.data.dao.MedicineDao;
import com.springboot.peanut.data.dto.medicine.MedicineRequestDto;
import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.data.entity.Intake;
import com.springboot.peanut.data.entity.Medicine;
import com.springboot.peanut.data.entity.User;
import com.springboot.peanut.jwt.JwtProvider;
import com.springboot.peanut.data.repository.UserRepository;
import com.springboot.peanut.jwt.JwtAuthenticationService;
import com.springboot.peanut.service.Result.ResultStatusService;
import com.springboot.peanut.service.User.MedicineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicineServiceImpl implements MedicineService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final MedicineDao medicineDao;
    private final IntakeDao intakeDao;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final ResultStatusService resultStatusService;

    @Override
    public ResultDto saveMedicineInfo(MedicineRequestDto medicineRequestDto, HttpServletRequest request) {
        User user = jwtAuthenticationService.authenticationToken(request);

        ResultDto resultDto = new ResultDto();


        if (user != null) {
            // Medicine 객체 생성
            Medicine medicine = Medicine.createMedicine(medicineRequestDto.getMedicineName(),user);
            medicine.setAlam(medicineRequestDto.isAlam());  // 필요에 따라 알람 설정
            medicineDao.saveMedicineInfo(medicine);
            log.info("[medicineInfo] : {}", medicine);

            // Intake 객체 생성 및 Medicine에 추가
            Intake intake = Intake.createIntake(
                    medicineRequestDto.getIntakeDays(),
                    medicineRequestDto.getIntakeTime(),
                    user,
                    medicine
            );
            medicine.addIntake(intake);
            intakeDao.saveIntakeInfo(intake);
            log.info("[intakeInfo] : {}", intake);

            resultDto.setDetailMessage("약 정보 입력 완료!");
            resultStatusService.setSuccess(resultDto);
        }else{
            resultStatusService. setFail(resultDto);
            throw new IllegalArgumentException();
        }

        return resultDto;
    }



}
