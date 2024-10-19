package com.springboot.peanut.data.dto.Insulin;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class InsulinRecordStatus {
    private List<InsulinRecordResponseDto> dailyStatuses; // 일별 혈당 상태 리스트
    private String monthlyStatusMessage; // 월 평균 상태 메시지

}
