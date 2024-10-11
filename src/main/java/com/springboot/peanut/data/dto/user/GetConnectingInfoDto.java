package com.springboot.peanut.data.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetConnectingInfoDto {
    private Long userId;
    private String userName;
    private String status;
}
