package com.springboot.peanut.data.dto.mainPage;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class PatientMainPageGetAdditionalInfoDto {
    private List<Map<String,Map<Integer, LocalDateTime>>> bloodSugarList;
    private String medicineName;
    private Boolean medicationState;
    private String medicineTime;
    private String insulinName;
    private Boolean insulinState;

}
