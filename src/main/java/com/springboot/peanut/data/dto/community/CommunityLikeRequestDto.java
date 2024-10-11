package com.springboot.peanut.data.dto.community;

import com.springboot.peanut.data.entity.Community;
import com.springboot.peanut.data.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommunityLikeRequestDto {

    private Long communityId;
    private boolean liked;
    private User user;
    private Community community;
    private LocalDateTime create_At;
    private LocalDateTime update_At;


}

