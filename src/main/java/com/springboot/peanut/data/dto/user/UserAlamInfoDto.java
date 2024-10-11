package com.springboot.peanut.data.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAlamInfoDto {
    private boolean guardianAlam;

    private boolean medicationAlam;

    private boolean insulinAlam;
}
