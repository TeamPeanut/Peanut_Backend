package com.springboot.peanut.data.dto.medicine;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class MedicineRequestDto {

    private String medicineName;

    private List<String> intakeDays;

    private List<String> intakeTime;

}
