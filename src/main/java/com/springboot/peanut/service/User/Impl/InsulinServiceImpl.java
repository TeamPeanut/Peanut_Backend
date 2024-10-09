package com.springboot.peanut.service.User.Impl;

import com.springboot.peanut.data.dao.InsulinDao;
import com.springboot.peanut.data.dto.Insulin.InsulinRecordResponseDto;
import com.springboot.peanut.data.dto.Insulin.InsulinRequestDto;
import com.springboot.peanut.data.dto.medicine.MedicineRecordResponseDto;
import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.data.entity.Insulin;
import com.springboot.peanut.data.entity.Intake;
import com.springboot.peanut.data.entity.Medicine;
import com.springboot.peanut.data.entity.User;
import com.springboot.peanut.jwt.JwtAuthenticationService;
import com.springboot.peanut.service.Result.ResultStatusService;
import com.springboot.peanut.service.User.InsulinService;
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
public class InsulinServiceImpl implements InsulinService {

    private final InsulinDao insulinDao;
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
}
