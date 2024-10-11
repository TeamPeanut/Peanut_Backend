package com.springboot.peanut.data.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUserInfoMyPage {
    private Long id;
    private String username;
    private String height;
    private String weight;
    private String profileUrl;
}
