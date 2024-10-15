package com.springboot.peanut.data.dto.Insulin;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class InsulinRecordResponseDto {
    private LocalDate recordDate;
    private String recordStatus;
}