package com.springboot.peanut.dto.Insulin;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class InsulinRequestDto {
    private String productName;

    private String dosage;

    private List<String> administrationTime;

    private boolean alam;
}
