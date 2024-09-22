package com.springboot.peanut.data.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserUpdateResponseDto {
    private Long userId;
    private String nickName;
    private String weight;
    private String height;
    private String imageUrl;
}
