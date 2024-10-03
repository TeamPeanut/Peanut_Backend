package com.springboot.peanut.data.repository.community.comment;

import com.springboot.peanut.data.entity.Comment;
import com.springboot.peanut.data.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentCustomRepository{
    Optional<List<Community>> findCommentCommunityById(Long userId);
}
