package com.springboot.peanut.service.Community.Impl;

import com.springboot.peanut.data.dao.CommunityDao;
import com.springboot.peanut.data.dao.CommunityLikeDao;
import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.data.entity.Community;
import com.springboot.peanut.data.entity.CommunityLike;
import com.springboot.peanut.data.entity.User;
import com.springboot.peanut.jwt.JwtAuthenticationService;
import com.springboot.peanut.service.Community.CommunityLikeService;
import com.springboot.peanut.service.Result.ResultStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityLikeServiceImpl implements CommunityLikeService {

    private final CommunityLikeDao communityLikeDao;
    private final CommunityDao communityDao;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final ResultStatusService resultStatusService;

    @Override
    public ResultDto like(Long communityId, boolean liked, HttpServletRequest request) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);
        log.info("[email] : {}" , user.get().getEmail());
        int cnt ;
        Community community = communityDao.getCommunityById(communityId);
        int communityLikeCnt = community.getCommunityLike();
        ResultDto resultDto = new ResultDto();
        if (community != null) {
            CommunityLike communityLike = CommunityLike.createLike(
                    true,
                    community,
                    user.get()
                    );
             community.setCommunityLike(communityLikeCnt+1);
             communityLikeDao.saveLike(communityLike);
             communityDao.saveCommunity(community);
             resultDto.setDetailMessage("좋아요 완료");
             resultStatusService.setSuccess(resultDto);
             return resultDto;
        }else{
            resultDto.setDetailMessage("해당하는 게시물이 없습니다.");
            resultStatusService.setFail(resultDto);
            throw new RuntimeException("해당하는 게시물이 없습니다.\"");
        }
    }
}
