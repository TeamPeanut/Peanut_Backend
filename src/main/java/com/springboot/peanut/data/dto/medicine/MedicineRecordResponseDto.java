package com.springboot.peanut.data.dto.medicine;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
@Getter
@AllArgsConstructor
public class MedicineRecordResponseDto {

    private Long id;
    private String medicineName;
    private String activeStatus;
    private List<String> intakeTime;
    private List<String> intakeDays;
}
