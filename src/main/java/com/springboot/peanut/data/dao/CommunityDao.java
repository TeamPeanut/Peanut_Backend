package com.springboot.peanut.data.dao;

import com.springboot.peanut.data.dto.community.CommunityResponseDto;
import com.springboot.peanut.data.entity.Community;

import java.util.List;

public interface CommunityDao {
    void saveCommunity(Community community);
    CommunityResponseDto getCommunityById(Long id);
    List<CommunityResponseDto> getAllCommunity();
}
