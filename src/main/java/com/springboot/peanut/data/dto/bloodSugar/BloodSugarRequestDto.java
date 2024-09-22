package com.springboot.peanut.data.dto.bloodSugar;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BloodSugarRequestDto {

    private String bloodSugarLevel;

    private String measurementTime;

    private String measurementCondition;

    private String memo;

}
