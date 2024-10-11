package com.springboot.peanut.data.dao;

import com.springboot.peanut.data.entity.Community;
import com.springboot.peanut.data.entity.CommunityLike;

import java.util.Optional;

public interface CommunityLikeDao {
    void saveLike(CommunityLike communityLike);
    void deleteLike(CommunityLike communityLike);
    Optional<CommunityLike> findCommunityLikeAndUser(Community community, Long userId);

}
