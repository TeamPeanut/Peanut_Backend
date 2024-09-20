package com.springboot.peanut.data.dto.community;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private Long userId;
    private String content;
    private String userName;
    private String imageUrl;
    private LocalDateTime createTime;

}
