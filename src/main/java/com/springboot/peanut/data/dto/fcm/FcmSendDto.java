package com.springboot.peanut.data.dto.fcm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.checkerframework.checker.units.qual.A;

@Getter
@ToString
@AllArgsConstructor
public class FcmSendDto {
    private String token;
    private String title;
    private String body;
}
