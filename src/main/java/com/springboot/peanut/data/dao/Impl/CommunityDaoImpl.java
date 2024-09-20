package com.springboot.peanut.data.dao.Impl;

import com.springboot.peanut.data.dao.CommunityDao;
import com.springboot.peanut.data.dto.community.CommentResponseDto;
import com.springboot.peanut.data.dto.community.CommunityDetailResponseDto;
import com.springboot.peanut.data.dto.community.CommunityResponseDto;
import com.springboot.peanut.data.entity.Community;
import com.springboot.peanut.data.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityDaoImpl implements CommunityDao {

    private final CommunityRepository communityRepository;

    @Override
    public void saveCommunity(Community community) {
        communityRepository.save(community);
    }

    @Override
    public CommunityDetailResponseDto getCommunityById(Long id) {
        Community community = communityRepository.findById(id).get();

        List<CommentResponseDto> commentDtos = community.getComments().stream()
                .map(comment -> new CommentResponseDto(
                        comment.getId(),
                        comment.getUser().getId(),
                        comment.getContent(),
                        comment.getUser().getUserName(),
                        comment.getUser().getProfileUrl(),
                        comment.getCreate_At()
                        )).collect(Collectors.toList());

        CommunityDetailResponseDto communityDetailResponseDto = new CommunityDetailResponseDto(
                community.getId(),
                community.getUser().getId(),
                community.getTitle(),
                community.getContent(),
                community.getUser().getProfileUrl(),
                community.getUser().getUserName(),
                community.getUser().getGender(),
                community.getCommunityLike(),
                commentDtos
        );

        return communityDetailResponseDto;
    }

    @Override
    public List<CommunityResponseDto> getAllCommunity() {
        List<CommunityResponseDto> communityResponseDtoList = new ArrayList<>();
        List<Community> communityList = communityRepository.findAll();
        for(Community community : communityList){
            CommunityResponseDto communityResponseDto = new CommunityResponseDto(
                    community.getId(),
                    community.getUser().getId(),
                    community.getTitle(),
                    community.getContent(),
                    community.getUser().getProfileUrl(),
                    community.getUser().getUserName(), community.getUser().getGender(),
                    community.getCommunityLike()
            );
             communityResponseDtoList.add(communityResponseDto);
        }
        return communityResponseDtoList;
    }
}
