package com.springboot.peanut.data.dto.user;

import lombok.Getter;

@Getter
public class UserUpdateRequestDto{
    private String nickName;
    private String weight;
    private String height;
}
