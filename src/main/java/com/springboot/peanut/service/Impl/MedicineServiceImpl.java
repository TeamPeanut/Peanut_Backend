package com.springboot.peanut.service.Impl;

import com.springboot.peanut.dao.IntakeDao;
import com.springboot.peanut.dao.MedicineDao;
import com.springboot.peanut.dto.CommonResponse;
import com.springboot.peanut.dto.Medicine.MedicineRequestDto;
import com.springboot.peanut.dto.signDto.ResultDto;
import com.springboot.peanut.entity.Intake;
import com.springboot.peanut.entity.Medicine;
import com.springboot.peanut.entity.User;
import com.springboot.peanut.jwt.JwtProvider;
import com.springboot.peanut.repository.IntakeRepository;
import com.springboot.peanut.repository.MedicineRepository;
import com.springboot.peanut.repository.UserRepository;
import com.springboot.peanut.service.MedicineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicineServiceImpl implements MedicineService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final MedicineDao medicineDao;
    private final IntakeDao intakeDao;

    @Override
    public ResultDto saveMedicineInfo(MedicineRequestDto medicineRequestDto, HttpServletRequest request) {
        String info = jwtProvider.getUsername(request.getHeader("X-AUTH-TOKEN"));
        User user = userRepository.getByEmail(info);
        log.info("[user] : {}",user);
        Medicine medicine = new Medicine();
        Intake intake = new Intake();
        ResultDto resultDto = new ResultDto();


        if(user != null){
            medicine.setMedicineName(medicineRequestDto.getMedicineName());
            medicine.setAlam(medicine.isAlam());
            medicine.setCreate_At(LocalDate.now());
            medicine.setUser(user);
            medicine.setCreate_At(LocalDate.now());
            medicineDao.saveMedicineInfo(medicine);
            log.info("[medicineInfo] : {}",medicine);

            intake.setIntakeTime(medicineRequestDto.getIntakeTime());
            intake.setIntakeDays(medicineRequestDto.getIntakeDays());
            intake.setIntakeNumber(medicineRequestDto.getIntakeNumber());
            intake.setCreate_At(LocalDate.now());
            intake.setUser(user);
            intake.setMedicine(medicine);
            intakeDao.saveIntakeInfo(intake);
            log.info("[intakeInfo] : {}",intake);

            resultDto.setDetailMessage("약 정보 입력 완료!");
            setSuccess(resultDto);
        }else{
            setFail(resultDto);
            throw new IllegalArgumentException();
        }

        return resultDto;
    }


    private void setSuccess(ResultDto resultDto) {
        resultDto.setSuccess(true);
        resultDto.setCode(CommonResponse.SUCCESS.getCode());
        resultDto.setMsg(CommonResponse.SUCCESS.getMsg());

    }

    private void setFail(ResultDto resultDto) {
        resultDto.setSuccess(false);
        resultDto.setCode(CommonResponse.Fail.getCode());
        resultDto.setMsg(CommonResponse.Fail.getMsg());
    }

}
