package com.springboot.peanut.service.User.Impl;

import com.springboot.peanut.data.dao.InsulinDao;
import com.springboot.peanut.data.dao.InsulinRecordDao;
import com.springboot.peanut.data.dto.Insulin.InsulinRecordResponseDto;
import com.springboot.peanut.data.dto.Insulin.InsulinReportResponseDto;
import com.springboot.peanut.data.dto.Insulin.InsulinRequestDto;
import com.springboot.peanut.data.dto.Insulin.InsulinReportStatus;
import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.data.entity.*;
import com.springboot.peanut.jwt.JwtAuthenticationService;
import com.springboot.peanut.service.Result.ResultStatusService;
import com.springboot.peanut.service.User.InsulinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsulinServiceImpl implements InsulinService {

    private final InsulinDao insulinDao;
    private final InsulinRecordDao insulinRecordDao;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final ResultStatusService resultStatusService;

    @Override
    public ResultDto saveInsulinInfo(InsulinRequestDto insulinRequestDto, HttpServletRequest request) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);

        ResultDto resultDto = new ResultDto();


        if (user != null){
            Insulin insulin = Insulin.createInsulin(insulinRequestDto,user.get());

            insulinDao.saveInsulin(insulin);
            resultDto.setDetailMessage("회원님의 인슐린 정보가 저장되었습니다.");
            resultStatusService.setSuccess(resultDto);

        }else {
            resultDto.setDetailMessage("인슐린 정보 저장 실패");
            resultStatusService.setFail(resultDto);
            throw  new IllegalArgumentException();
        }
        return resultDto;
    }
    @Override
    public List<InsulinRecordResponseDto> getInsulinInfoList(HttpServletRequest request) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);
        if (user.isPresent()) {
            Insulin insulin = insulinDao.getInsulinByUserId(user.get().getId());
            List<InsulinRecordResponseDto> insulinRecordResponseDtoList = new ArrayList<>();
            List<String> administrationTime = insulin.getAdministrationTime();

            InsulinRecordResponseDto insulinRecordResponseDto = new InsulinRecordResponseDto(
                    insulin.getId(),
                    insulin.getProductName(),
                    insulin.getDosage(),
                    administrationTime

            );
            insulinRecordResponseDtoList.add(insulinRecordResponseDto);
            return insulinRecordResponseDtoList;
        }else{
            throw new IllegalArgumentException("투약 인슐린이 없습니다.");
        }


    }
    @Override
    public InsulinReportStatus getInsulinInfoList(int year, int month, HttpServletRequest request) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);
        if (user.isPresent()) {
            // 해당 유저의 년도와 달에 따른 인슐린 기록을 가져옵니다
            List<InsulinRecord> insulinRecordList = insulinRecordDao.findInsulinByYearAndMonth(user.get().getId(), year, month);
            List<InsulinReportResponseDto> insulinReportResponseDtoList = new ArrayList<>();
            int cnt = 0;

            // 각 날짜에 대해 일일 측정 횟수(cnt)를 계산합니다.
            for (InsulinRecord insulinRecord : insulinRecordList) {
                LocalDate date = insulinRecord.getRecordDate();
                cnt++;
                String recordStatus = cntStatus(cnt);

                // 해당 날짜의 응답 DTO 생성
                InsulinReportResponseDto insulinRecordResponseDto = new InsulinReportResponseDto(
                        date,
                        recordStatus
                );
                insulinReportResponseDtoList.add(insulinRecordResponseDto);
            }
            String monthlyReport = monthlyReport(cnt);
            InsulinReportStatus insulinReportStatus = new InsulinReportStatus(
                    insulinReportResponseDtoList,
                    monthlyReport

            );

            return insulinReportStatus;
        } else {
            throw new IllegalArgumentException("투약 인슐린 정보가 없습니다.");
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
            return "총 투여일의" +cnt+"일을 투여했어요! 건강을 위해서라도 더 신경써서 투약하는게 어떨까요?";
        }else if(10<=cnt&&cnt<15){
            return "총 투여일의" +cnt+"일을 투여했어요! 건강을 생각하며 더 복약하면 좋을 것 같아요! ";
        }else if(15<=cnt&&cnt<20){
            return "총 투여일의" +cnt+"일을 투여했어요! 이번달 절반 이상 투여했어요! 조금만 더 노력하여 건강을 챙겨보아요!";
        }else if(20<=cnt&&cnt<31) {
            return "총 투여일의" + cnt + "일을 투여했어요! 매우 잘했어요! 앞으로 더욱 건강한 생활이 가능할 거예요!";
        }else {
            return null;
        }
    }

}