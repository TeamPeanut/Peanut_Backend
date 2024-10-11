package com.springboot.peanut.data.dto.Insulin;

import com.springboot.peanut.data.dto.bloodSugar.DailyBloodSugarStatus;

import java.util.List;

public class MonthlyInsulinStatus {
    private String monthlyStatusMessage; // 월 평균 상태 메시지
    private List<DailyInsulinStatus> dailyStatuses; // 일별 혈당 상태 리스트

}
