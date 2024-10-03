package com.springboot.peanut.data.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PatientConnectingResponse {
    private String name;
    private String gender;
    private String phoneNumber;
    private String birthday;
    private String profileImage;
}
