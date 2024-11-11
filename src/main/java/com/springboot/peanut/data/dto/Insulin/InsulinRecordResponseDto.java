package com.springboot.peanut.data.dto.Insulin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.checkerframework.checker.units.qual.A;

import java.util.List;

@Getter
@AllArgsConstructor
public class InsulinRecordResponseDto {
    private Long id;
    private String productName;
    private String activeStatus;
    private String dosage;
    private List<String> administrationTime;

}
