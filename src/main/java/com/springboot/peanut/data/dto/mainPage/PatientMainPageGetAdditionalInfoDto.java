package com.springboot.peanut.data.dto.mainPage;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientMainPageGetAdditionalInfoDto {
    private List<Map<Integer, LocalDateTime>> bloodSugarList;
    private String medicineName;
    private Boolean medicationState;
    private String insulinName;
    private Boolean insulinState;


    public void MainPageGetAdditionalInfoDto(){
        this.bloodSugarList = bloodSugarList;
        this.medicineName = medicineName;
        this.medicationState = medicationState;
        this.insulinName = insulinName;
        this.insulinState = insulinState;
    }
}
