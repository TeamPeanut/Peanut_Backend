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
        log.info("[email] : {}", user.get().getEmail());
        Community community = communityDao.getCommunityById(communityId);
        int communityLikeCnt = community.getCommunityLike();
        ResultDto resultDto = new ResultDto();
        Optional<CommunityLike> existingLike = communityLikeDao.findCommunityLikeAndUser(community, user.get().getId());
        if (existingLike.isPresent()) {
            CommunityLike communityLike = existingLike.get();
            if (liked) {
                // 이미 좋아요 상태인데 또 눌렀다면 중복 방지
                resultDto.setDetailMessage("이미 좋아요를 눌렀습니다.");
                resultStatusService.setFail(resultDto);
            } else {
                communityLikeDao.deleteLike(communityLike);
                community.setCommunityLike(communityLikeCnt - 1);
                communityDao.saveCommunity(community);  // 커뮤니티 업데이트
                resultDto.setDetailMessage("좋아요를 취소했습니다.");
                resultStatusService.setSuccess(resultDto);
            }
        } else {
            if (liked) {
                // 좋아요 추가
                CommunityLike communityLike = CommunityLike.createLike(
                        true,
                        community,
                        user.get()
                );
                community.setCommunityLike(communityLikeCnt + 1); // 좋아요 수 증가
                communityLikeDao.saveLike(communityLike); // 좋아요 저장
                communityDao.saveCommunity(community);  // 커뮤니티 업데이트
                resultDto.setDetailMessage("좋아요 완료");
                resultStatusService.setSuccess(resultDto);
            } else {
                // 좋아요가 없는 상태에서 취소를 눌렀다면 에러 처리
                resultDto.setDetailMessage("좋아요가 눌려있지 않습니다.");
                resultStatusService.setFail(resultDto);
            }
        }
        return resultDto;
        }
    }

