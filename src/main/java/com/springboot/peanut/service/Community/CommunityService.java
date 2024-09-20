package com.springboot.peanut.service.Community;

import com.springboot.peanut.data.dto.community.CommunityRequestDto;
import com.springboot.peanut.data.dto.community.CommunityResponseDto;
import com.springboot.peanut.data.dto.signDto.ResultDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CommunityService {
    ResultDto createCommunity(CommunityRequestDto communityRequestDto, HttpServletRequest request);
    CommunityResponseDto detailsCommunity(Long id, HttpServletRequest request);
    List<CommunityResponseDto>getAllCommunity(HttpServletRequest request);
}
