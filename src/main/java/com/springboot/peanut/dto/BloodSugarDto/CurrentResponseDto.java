package com.springboot.peanut.dto.BloodSugarDto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CurrentResponseDto {
    private Long id;
    private int current_blood_sugar;
    private LocalDate record_time;

}
