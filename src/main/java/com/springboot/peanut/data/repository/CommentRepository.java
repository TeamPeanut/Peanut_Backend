package com.springboot.peanut.data.repository;

import com.springboot.peanut.data.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
