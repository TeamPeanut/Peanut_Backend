package com.springboot.peanut.data.dto.Insulin;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InsulinResponseDto {
    private Long id;
    private String productName;

    private String dosage;

    private List<String> administrationTime;

    private boolean alam;
}
