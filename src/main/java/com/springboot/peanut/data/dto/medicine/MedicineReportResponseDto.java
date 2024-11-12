package com.springboot.peanut.data.dto.medicine;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class MedicineReportResponseDto {
    private LocalDate recordDate;
    private String recordStatus;

}