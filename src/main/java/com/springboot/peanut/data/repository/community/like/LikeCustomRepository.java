package com.springboot.peanut.data.repository.community.like;

import com.springboot.peanut.data.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeCustomRepository {
    Optional<List<Community>> findLikeCommunityById(Long userId);
}
