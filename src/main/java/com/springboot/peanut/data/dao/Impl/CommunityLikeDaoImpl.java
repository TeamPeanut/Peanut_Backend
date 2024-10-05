package com.springboot.peanut.data.dao.Impl;

import com.springboot.peanut.data.dao.CommunityLikeDao;
import com.springboot.peanut.data.entity.Community;
import com.springboot.peanut.data.entity.CommunityLike;
import com.springboot.peanut.data.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunityLikeDaoImpl implements CommunityLikeDao {

    private final LikeRepository likeRepository;

    @Override
    public void saveLike(CommunityLike communityLike) {
        likeRepository.save(communityLike);
    }

    @Override
    public void deleteLike(CommunityLike communityLike) {
        likeRepository.delete(communityLike);
    }

    @Override
    public Optional<CommunityLike> findCommunityLikeAndUser(Community community, Long userId) {
        return likeRepository.findByCommunityAndUserId(community, userId);
    }
}
