package com.springboot.peanut.data.dto.bloodSugar;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MonthlyBloodSugarStatus {
    private double monthlyAvg; // 월 평균 수치
    private String monthlyAvgStatus; // 월 평균 수치
    private String monthlyStatusMessage; // 월 평균 상태 메시지
    private List<DailyBloodSugarStatus> dailyStatuses; // 일별 혈당 상태 리스트

}
