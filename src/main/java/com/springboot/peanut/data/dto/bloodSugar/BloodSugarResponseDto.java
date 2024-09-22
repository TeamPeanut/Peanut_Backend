package com.springboot.peanut.data.dto.bloodSugar;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BloodSugarResponseDto {
    private Long id;

    private String current_blood_sugar;

    private String measurementTime;

    private String memo;

}
