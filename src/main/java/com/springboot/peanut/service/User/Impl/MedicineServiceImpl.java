package com.springboot.peanut.service.User.Impl;

import com.springboot.peanut.data.dao.IntakeDao;
import com.springboot.peanut.data.dao.MedicineDao;
import com.springboot.peanut.data.dao.MedicineRecordDao;
import com.springboot.peanut.data.dto.Insulin.InsulinRecordResponseDto;
import com.springboot.peanut.data.dto.medicine.MedicineRecordResponseDto;
import com.springboot.peanut.data.dto.medicine.MedicineRecordStatus;
import com.springboot.peanut.data.dto.medicine.MedicineRequestDto;
import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.data.entity.Intake;
import com.springboot.peanut.data.entity.Medicine;
import com.springboot.peanut.data.entity.MedicineRecord;
import com.springboot.peanut.data.entity.User;
import com.springboot.peanut.jwt.JwtAuthenticationService;
import com.springboot.peanut.service.Result.ResultStatusService;
import com.springboot.peanut.service.User.MedicineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicineServiceImpl implements MedicineService {


    private final MedicineDao medicineDao;
    private final MedicineRecordDao medicineRecordDao;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final ResultStatusService resultStatusService;
    private final IntakeDao intakeDao;

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
    public MedicineRecordStatus getMedicineInfoList(int year, int month, HttpServletRequest request) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);
        if (user.isPresent()) {
            List<MedicineRecord> medicineRecordList = medicineRecordDao.findMedicineByYearAndMonth(user.get().getId(), year, month);
            List<MedicineRecordResponseDto> medicineRecordResponseDtoList = new ArrayList<>();
            int cnt = 0;
            // 각 약에 대해 반복
            for (MedicineRecord m : medicineRecordList) {
                LocalDate date = m.getRecordDate();
                cnt++;
                String recordStatus = cntStatus(cnt);

                // 각 약에 대한 DTO 생성
                MedicineRecordResponseDto medicineRecordResponseDto = new MedicineRecordResponseDto(
                        date,
                        recordStatus
                );
                medicineRecordResponseDtoList.add(medicineRecordResponseDto);
            }
            String monthlyReport = monthlyReport(cnt);
            MedicineRecordStatus medicineRecordStatus = new MedicineRecordStatus(
                    medicineRecordResponseDtoList,
                    monthlyReport
            );
         return medicineRecordStatus;
        }else{
            throw new IllegalArgumentException("복용 약이 없습니다.");
        }

    }
    // 매일 측정 상태를 반환하는 메서드
    public String cntStatus(int cnt) {
        if (cnt == 0) {
            return "아쉬워요";
        } else if (cnt == 1 || cnt == 2) {
            return "보통이에요";
        } else if (cnt >= 3) {
            return "참 잘했어요";
        } else {
            return null;
        }
    }


    public String monthlyReport(int cnt){

        if(0<=cnt&&cnt<10){
            return "총 복약량의" +cnt+"일을 복약했어요! 건강을 위해서라도 더 신경써서 복약하는게 어떨까요?";
        }else if(10<=cnt&&cnt<15){
            return "총 복약량의" +cnt+"일을 복약했어요! 건강을 생각하며 더 복약하면 좋을 것 같아요! ";
        }else if(15<=cnt&&cnt<20){
            return "총 복약량의" +cnt+"일을 복약했어요! 이번달 절반 이상 복약했어요! 조금만 더 노력하여 건강을 챙겨보아요!";
        }else if(20<=cnt&&cnt<31) {
            return "총 복약량의" + cnt + "일을 복약했어요! 매우 잘했어요! 앞으로 더욱 건강한 생활이 가능할 거예요!";
        }else {
            return null;
        }
    }

}
