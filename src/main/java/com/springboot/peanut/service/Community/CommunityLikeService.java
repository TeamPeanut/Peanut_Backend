package com.springboot.peanut.service.Community;

import com.springboot.peanut.data.dto.signDto.ResultDto;

import javax.servlet.http.HttpServletRequest;

public interface CommunityLikeService {
    ResultDto like(Long communityId, boolean liked, HttpServletRequest request);
}
