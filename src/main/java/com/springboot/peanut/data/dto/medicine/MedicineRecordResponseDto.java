package com.springboot.peanut.data.dto.medicine;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
public class MedicineRecordResponseDto {
    private LocalDate recordDate;
    private String recordStatus;

}