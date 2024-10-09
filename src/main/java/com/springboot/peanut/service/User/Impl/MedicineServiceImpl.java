package com.springboot.peanut.service.User.Impl;

import com.springboot.peanut.data.dao.IntakeDao;
import com.springboot.peanut.data.dao.MedicineDao;
import com.springboot.peanut.data.dto.medicine.MedicineRecordResponseDto;
import com.springboot.peanut.data.dto.medicine.MedicineRequestDto;
import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.data.entity.FoodNutrition;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicineServiceImpl implements MedicineService {


    private final MedicineDao medicineDao;
    private final IntakeDao intakeDao;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final ResultStatusService resultStatusService;

    @Override
    public ResultDto saveMedicineInfo(MedicineRequestDto medicineRequestDto, HttpServletRequest request) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);

        ResultDto resultDto = new ResultDto();


        if (user.isPresent()) {
            // Medicine 객체 생성
            Medicine medicine = Medicine.createMedicine(medicineRequestDto.getMedicineName(),user.get());
            medicineDao.saveMedicineInfo(medicine);
            log.info("[medicineInfo] : {}", medicine);

            // Intake 객체 생성 및 Medicine에 추가
            Intake intake = Intake.createIntake(
                    medicineRequestDto.getIntakeDays(),
                    medicineRequestDto.getIntakeTime(),
                    user.get(),
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

    @Override
    public List<MedicineRecordResponseDto> getMedicineInfoList(HttpServletRequest request) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);
        if (user.isPresent()) {
            List<Medicine> medicine = medicineDao.getMedicineByUserId(user.get().getId());
            List<MedicineRecordResponseDto> medicineRecordResponseDtoList = new ArrayList<>();

            // 각 약에 대해 반복
            for (Medicine m : medicine) {
                List<Intake> intakeList = m.getIntakes();

                // Intake 리스트에서 intakeDays를 추출하여 하나의 리스트로 병합
                List<String> allIntakeDays = intakeList.stream()
                        .flatMap(intake -> intake.getIntakeDays().stream())
                        .collect(Collectors.toList());

                // Intake 리스트에서 intakeTime을 추출하여 하나의 리스트로 병합
                List<String> allIntakeTimes = intakeList.stream()
                        .flatMap(intake -> intake.getIntakeTime().stream())
                        .collect(Collectors.toList());

                // 각 약에 대한 DTO 생성
                MedicineRecordResponseDto medicineRecordResponseDto = new MedicineRecordResponseDto(
                        m.getId(),
                        m.getMedicineName(),
                        allIntakeDays,
                        allIntakeTimes
                );

                // DTO를 리스트에 추가
                medicineRecordResponseDtoList.add(medicineRecordResponseDto);
            }
         return medicineRecordResponseDtoList;
        }else{
            throw new IllegalArgumentException("복용 약이 없습니다.");
        }


    }
}
