package com.springboot.peanut.data.dao;

import com.springboot.peanut.data.dto.community.CommunityDetailResponseDto;
import com.springboot.peanut.data.dto.community.CommunityResponseDto;
import com.springboot.peanut.data.entity.Community;

import java.util.List;

public interface CommunityDao {
    void saveCommunity(Community community);
    CommunityDetailResponseDto findCommunityById(Long id);
    Community getCommunityById(Long id);
    List<CommunityResponseDto> getAllCommunity();
}
