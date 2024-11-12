package com.springboot.peanut.data.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.checkerframework.checker.units.qual.A;

@Getter
@AllArgsConstructor
public class UpdateUserInfoResponseDto {
    private Long userId;
    private String phoneNumber;
    private String gender;
    private String birthday;
    private String userName;
}
