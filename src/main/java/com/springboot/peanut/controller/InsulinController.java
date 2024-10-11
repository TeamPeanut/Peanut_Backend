package com.springboot.peanut.controller;

import com.springboot.peanut.data.dto.Insulin.InsulinRecordResponseDto;
import com.springboot.peanut.data.dto.Insulin.InsulinRequestDto;
import com.springboot.peanut.data.dto.medicine.MedicineRecordResponseDto;
import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.service.User.InsulinService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/insulin")
@RequiredArgsConstructor
public class InsulinController {

    private final InsulinService insulinService;

    @PostMapping("/save")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<ResultDto> saveInsulinInfo(InsulinRequestDto insulinRequestDto, HttpServletRequest request){
        ResultDto resultDto = insulinService.saveInsulinInfo(insulinRequestDto,request);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }
    @GetMapping("/get/record")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<List<InsulinRecordResponseDto>> getInsulinInfoList(HttpServletRequest request) {
        List<InsulinRecordResponseDto> insulinRecordResponseDtos = insulinService.getInsulinInfoList(request);
        return ResponseEntity.status(HttpStatus.OK).body(insulinRecordResponseDtos);
    }
}
