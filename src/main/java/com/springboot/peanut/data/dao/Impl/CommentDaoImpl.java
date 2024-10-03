package com.springboot.peanut.data.dao.Impl;

import com.springboot.peanut.data.dao.CommentDao;
import com.springboot.peanut.data.entity.Comment;
import com.springboot.peanut.data.repository.community.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentDaoImpl implements CommentDao{

    private final CommentRepository commentRepository;

    @Override
    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }
}
