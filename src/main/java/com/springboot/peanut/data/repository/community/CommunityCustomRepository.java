package com.springboot.peanut.data.repository.community;

import com.springboot.peanut.data.entity.Community;

import java.util.List;
import java.util.Optional;

public interface CommunityCustomRepository {
    Optional<List<Community>> findCreateCommunityById(Long id);
    List<Community> findCommentCommunityByUserId(Long userId);
    List<Community> findLikeCommunityByUserId(Long userId);

}
