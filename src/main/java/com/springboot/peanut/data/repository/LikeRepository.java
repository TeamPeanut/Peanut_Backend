package com.springboot.peanut.data.repository;

import com.springboot.peanut.data.dto.community.CommunityLikeRequestDto;
import com.springboot.peanut.data.entity.Community;
import com.springboot.peanut.data.entity.CommunityLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<CommunityLike,Long> {
    CommunityLike save(CommunityLikeRequestDto communityLikeRequestDto);
    boolean existsByUserIdAndLikedTrue(Long userId);
    Optional<CommunityLike> findByCommunityAndUserId(Community community, Long userId);
}
