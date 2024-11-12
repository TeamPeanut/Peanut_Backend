package com.springboot.peanut.data.dto.user.Requset;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateUserInfoDto {
    private String phoneNumber;
    private String gender;
    private String birthday;
    private String userName;
    private String password;
}
