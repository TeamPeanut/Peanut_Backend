package com.springboot.peanut.data.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetPatientResponseDto {
    private Long id;
    private String userName;
    private String gender;
    private String birthday;
    private String height;
    private String weight;
    private String profileUrl;
}
