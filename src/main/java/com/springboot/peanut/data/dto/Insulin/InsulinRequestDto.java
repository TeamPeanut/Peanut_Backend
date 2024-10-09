package com.springboot.peanut.data.dto.Insulin;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class InsulinRequestDto {
    private String productName;

    private String dosage;

    private List<String> administrationTime;

}
