package com.springboot.peanut.dto.BloodSugarDto;

import com.springboot.peanut.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Getter
@Setter
public class FastingResponseDto {
    private Long id;
    private int fasting_blood_sugar;
    private LocalDate record_time;
}
