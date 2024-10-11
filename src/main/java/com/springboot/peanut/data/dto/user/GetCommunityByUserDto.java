package com.springboot.peanut.data.dto.user;

import com.springboot.peanut.data.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetCommunityByUserDto {
    private String title;
    private String content;
    private int comment;
    private int like;
    private LocalDateTime create_At;
    private String userName;
}
