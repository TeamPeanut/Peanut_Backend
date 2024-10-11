package com.springboot.peanut.data.dto.bloodSugar;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DailyBloodSugarStatus {
    private LocalDate date;
    private String bloodSugarStatus;
}
