package com.springboot.peanut.data.dao;

import com.springboot.peanut.data.entity.Comment;

public interface CommentDao {
    void saveComment(Comment comment);
}
