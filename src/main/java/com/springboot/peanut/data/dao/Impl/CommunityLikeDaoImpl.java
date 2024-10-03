package com.springboot.peanut.data.dao.Impl;

import com.springboot.peanut.data.dao.CommunityLikeDao;
import com.springboot.peanut.data.entity.CommunityLike;
import com.springboot.peanut.data.repository.community.like.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommunityLikeDaoImpl implements CommunityLikeDao {
    private final LikeRepository likeRepository;

    @Override
    public void saveLike(CommunityLike communityLike) {
        likeRepository.save(communityLike);
    }
}
