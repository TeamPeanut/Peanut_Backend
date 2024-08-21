package com.springboot.peanut.dto.bloodSugar;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BloodSugarRequestDto {

    private String blood_sugar;

    private String measurementTime;

    private String memo;

}
