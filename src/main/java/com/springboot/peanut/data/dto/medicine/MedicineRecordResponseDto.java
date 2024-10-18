package com.springboot.peanut.data.dto.medicine;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class MedicineRecordResponseDto {
    private Long id;
    private String medicineName;
    private List<String> allIntakeDays;
    private List<String> allIntakeTimes;

}