package com.springboot.peanut.data.dto.community;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommunityRequestDto {
    private String title;
    private String content;
}
