package com.springboot.peanut.dto.mainPage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MainPageGetUserDto {
    private Long userId;
    private String userName;
    private String profileUrl;
    private String fastingBloodSugarLevel;
    private String currentBloodSugarLevel;


//    public void MainPageGetUserDto(Long id, String userName, String profileUrl, String fastingBloodSugarLevel, String currentBloodSugarLevel) {
//        this.userId = id;
//        this.userName = userName;
//        this.profileUrl = profileUrl;
//        this.fastingBloodSugarLevel = fastingBloodSugarLevel;
//        this.currentBloodSugarLevel = currentBloodSugarLevel;
//    }

}


