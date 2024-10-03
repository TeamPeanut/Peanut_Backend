package com.springboot.peanut.data.dao.Impl;

import com.springboot.peanut.data.dao.CommunityDao;
import com.springboot.peanut.data.dto.community.CommentResponseDto;
import com.springboot.peanut.data.dto.community.CommunityDetailResponseDto;
import com.springboot.peanut.data.dto.community.CommunityResponseDto;
import com.springboot.peanut.data.dto.user.GetCommunityByUserDto;
import com.springboot.peanut.data.entity.Community;
import com.springboot.peanut.data.repository.CommentRepository;
import com.springboot.peanut.data.repository.community.CommunityRepository;
import com.springboot.peanut.data.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityDaoImpl implements CommunityDao {

    private final CommunityRepository communityRepository;

    @Override
    public void saveCommunity(Community community) {
        communityRepository.save(community);
    }

    @Override
    public CommunityDetailResponseDto findCommunityById(Long id) {
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

    @Override
    public List<GetCommunityByUserDto> getCreateAllCommunityByUser(Long userId) {
        List<GetCommunityByUserDto> getCommunityByUserList = new ArrayList<>();
        Optional<List<Community>> communityList = communityRepository.findCreateCommunityById(userId);
        for(Community community : communityList.get()){
            GetCommunityByUserDto getCommunityByUserDto = new GetCommunityByUserDto(
                    community.getTitle(),
                    community.getContent(),
                    community.getComments().size(),
                    community.getCommunityLike(),
                    community.getCreate_At(),
                    community.getUser().getUserName()
            );

            getCommunityByUserList.add(getCommunityByUserDto);
        }
        return getCommunityByUserList;
    }

    @Override
    public List<GetCommunityByUserDto> getCommentAllCommunityByUser(Long userId) {
        List<GetCommunityByUserDto> getCommunityByUserList = new ArrayList<>();
        List<Community> communityList = communityRepository.findCommentCommunityByUserId(userId) ;
        for (Community community : communityList) {
            GetCommunityByUserDto getCommunityByUserDto = new GetCommunityByUserDto(
                    community.getTitle(),
                    community.getContent(),
                    community.getComments().size(),
                    community.getCommunityLike(),
                    community.getCreate_At(),
                    community.getUser().getUserName()
            );
            getCommunityByUserList.add(getCommunityByUserDto);
        }
        return getCommunityByUserList;
    }

    @Override
    public List<GetCommunityByUserDto> getLikeAllCommunityByUser(Long userId) {
        List<GetCommunityByUserDto> getCommunityByUserList = new ArrayList<>();
        List<Community> communityList = communityRepository.findLikeCommunityByUserId(userId);
        for(Community community : communityList){
            GetCommunityByUserDto getCommunityByUserDto = new GetCommunityByUserDto(
                    community.getTitle(),
                    community.getContent(),
                    community.getComments().size(),
                    community.getCommunityLike(),
                    community.getCreate_At(),
                    community.getUser().getUserName()
            );

            getCommunityByUserList.add(getCommunityByUserDto);
        }
        return getCommunityByUserList;
    }

    @Override
    public Community getCommunityById(Long id) {
        return communityRepository.getById(id);
    }
}
