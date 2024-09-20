package com.springboot.peanut.service.Community.Impl;

import com.springboot.peanut.data.dao.CommunityDao;
import com.springboot.peanut.data.dto.community.CommunityRequestDto;
import com.springboot.peanut.data.dto.community.CommunityResponseDto;
import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.data.entity.Community;
import com.springboot.peanut.data.entity.User;
import com.springboot.peanut.service.Community.CommunityService;
import com.springboot.peanut.jwt.JwtAuthenticationService;
import com.springboot.peanut.service.Result.ResultStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommunityServiceImpl implements CommunityService {

    private final JwtAuthenticationService jwtAuthenticationService;
    private final ResultStatusService resultStatusService;
    private final CommunityDao communityDao;


    @Override
    public ResultDto createCommunity(CommunityRequestDto communityRequestDto, HttpServletRequest request) {
        User user = jwtAuthenticationService.authenticationToken(request);
        ResultDto resultDto = new ResultDto();
        if(user != null){
            Community community = Community.createCommunity(communityRequestDto, user);
            communityDao.saveCommunity(community);
            resultDto.setDetailMessage("글 작성 완료");
            resultStatusService.setSuccess(resultDto);
        }else{
            resultStatusService.setFail(resultDto);
            throw new RuntimeException("글 작성 실패");
        }
        return resultDto;
    }

    @Override
    public CommunityResponseDto detailsCommunity(Long id, HttpServletRequest request) {
        User user = jwtAuthenticationService.authenticationToken(request);
        log.info("[user] : {} " , user);
        CommunityResponseDto communityResponseDto = communityDao.getCommunityById(id);
        return communityResponseDto;
    }

    @Override
    public List<CommunityResponseDto> getAllCommunity(HttpServletRequest request) {
        List<CommunityResponseDto> communityResponseDtoList = communityDao.getAllCommunity();
        return communityResponseDtoList;
    }
}
