package com.springboot.peanut.data.dto.community;

import com.springboot.peanut.data.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CommunityResponseDto {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String imageUrl;
    private String name;
    private String gender;
    private int like;
}
