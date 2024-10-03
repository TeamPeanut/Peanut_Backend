package com.springboot.peanut.data.repository.community.comment;

import com.springboot.peanut.data.entity.Comment;
import com.springboot.peanut.data.repository.community.community.CommunityCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long>, CommentCustomRepository {
}
