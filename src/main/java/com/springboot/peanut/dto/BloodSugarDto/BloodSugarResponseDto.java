package com.springboot.peanut.dto.BloodSugarDto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BloodSugarResponseDto {
    private Long id;

    private String current_blood_sugar;

    private String measurementTime;

    private String memo;

}
