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
public class GuardianMainPageGetAdditionalInfoDto {
    private List<Map<Integer, LocalDateTime>> bloodSugarList;
    private String medicineName;
    private Boolean medicineAlam;
    private String insulinName;
    private Boolean insulinAlam;

    public void MainPageGetAdditionalInfoDto(){
        this.bloodSugarList = bloodSugarList;
        this.medicineName = medicineName;
        this.medicineAlam = medicineAlam;
        this.insulinName = insulinName;
        this.insulinAlam = insulinAlam;
    }
}
