package com.springboot.peanut.controller;

import com.springboot.peanut.data.dto.medicine.MedicineRecordResponseDto;
import com.springboot.peanut.data.dto.medicine.MedicineReportStatus;
import com.springboot.peanut.data.dto.medicine.MedicineRequestDto;
import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.service.User.MedicineService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/medicine")
@RequiredArgsConstructor
public class MedicineController {
    private final MedicineService medicineService;

    @PostMapping("/save")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<ResultDto> saveMedicineInfo(MedicineRequestDto medicineRequestDto, HttpServletRequest request){
        ResultDto resultDto = medicineService.saveMedicineInfo(medicineRequestDto,request);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }


    @GetMapping("/get/report")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<MedicineReportStatus> getMedicineInfoList(int year, int month, HttpServletRequest request) {
        MedicineReportStatus medicineReportStatuses = medicineService.getMedicineInfoList(year, month, request);
        return ResponseEntity.status(HttpStatus.OK).body(medicineReportStatuses);
    }

    @GetMapping("/get/guardian-report")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<MedicineReportStatus> getGuardianMedicineInfoList(int year, int month, HttpServletRequest request) {
        MedicineReportStatus medicineReportStatuses = medicineService.getGuardianMedicineInfoList(year, month, request);
        return ResponseEntity.status(HttpStatus.OK).body(medicineReportStatuses);
    }


    @GetMapping("/get/record")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<List<MedicineRecordResponseDto>> getMedicineInfoList(HttpServletRequest request){
        List<MedicineRecordResponseDto> medicineRecordResponseDtoList = medicineService.getMedicineInfoList(request);
        return ResponseEntity.status(HttpStatus.OK).body(medicineRecordResponseDtoList);
    }


    @PutMapping("/update-status/record")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<ResultDto> stopMedicine(Long medicineId, boolean activeStatus, HttpServletRequest request){
        ResultDto resultDto = medicineService.stopMedicine(medicineId,activeStatus,request);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }

}
