package com.springboot.peanut.dto.BloodSugarDto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BloodSugarRequestDto {

    private String blood_sugar;

    private String measurementTime;

    private String memo;

}
