package com.springboot.peanut.dto.Medicine;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.ElementCollection;
import java.util.List;

@Data
@Getter
@Setter
public class MedicineRequestDto {

    private String medicineName;

    private boolean alam = false;

    private List<String> intakeDays;

    private String intakeNumber;

    private List<String> intakeTime;

}
