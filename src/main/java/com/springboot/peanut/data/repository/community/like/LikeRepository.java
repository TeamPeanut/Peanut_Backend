package com.springboot.peanut.data.repository.community.like;

import com.springboot.peanut.data.dto.community.CommunityLikeRequestDto;
import com.springboot.peanut.data.entity.Comment;
import com.springboot.peanut.data.entity.CommunityLike;
import com.springboot.peanut.data.repository.community.comment.CommentCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<CommunityLike,Long>, LikeCustomRepository {
    CommunityLike save(CommunityLikeRequestDto communityLikeRequestDto);
}
