package com.springboot.peanut.data.dto.mainPage;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class GuardianMainPageGetAdditionalInfoDto {
    private List<Map<String,Map<Integer, LocalDateTime>>> bloodSugarList;
    private String medicineName;
    private Boolean medicationAlam;
    private String insulinName;
    private Boolean insulinAlam;


    public void MainPageGetAdditionalInfoDto(){
        this.bloodSugarList = bloodSugarList;
        this.medicineName = medicineName;
        this.medicationAlam = medicationAlam;
        this.insulinName = insulinName;
        this.insulinAlam = insulinAlam;
    }
}
