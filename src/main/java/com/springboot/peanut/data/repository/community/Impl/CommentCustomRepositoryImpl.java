package com.springboot.peanut.data.repository.community.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.peanut.data.entity.Community;
import com.springboot.peanut.data.entity.QComment;
import com.springboot.peanut.data.entity.QCommunity;
import com.springboot.peanut.data.repository.community.comment.CommentCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentCustomRepositoryImpl implements CommentCustomRepository {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<List<Community>> findCommentCommunityById(Long userId) {
        QCommunity qCommunity = QCommunity.community;
        QComment qComment = QComment.comment;

        return Optional.ofNullable(jpaQueryFactory
                .select(qCommunity)
                .from(qComment) // qComment를 기준으로 시작
                .join(qComment.community, qCommunity) // 댓글과 관련된 커뮤니티를 조인
                .where(qComment.user.id.eq(userId)) // 사용자가 작성한 댓글로 필터링
                .fetch());
    }
}
