package com.springboot.peanut.data.repository.community.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.peanut.data.entity.Community;
import com.springboot.peanut.data.entity.QComment;
import com.springboot.peanut.data.entity.QCommunity;
import com.springboot.peanut.data.entity.QCommunityLike;
import com.springboot.peanut.data.repository.community.CommunityCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunityCustomRepositoryImpl implements CommunityCustomRepository {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<List<Community>> findCreateCommunityById(Long id) {
        QCommunity community = QCommunity.community;


        return Optional.ofNullable(jpaQueryFactory.selectFrom(community)
                .where(community.user.id.eq(id))
                .fetch());
    }
    @Override
    public List<Community> findCommentCommunityByUserId(Long userId) {
        QCommunity qCommunity = QCommunity.community;
        QComment qComment = QComment.comment;

        return jpaQueryFactory
                .selectFrom(qCommunity)
                .join(qCommunity.comments, qComment) // Community의 Comment와 조인
                .where(qComment.user.id.eq(userId))  // Comment의 user.id와 일치하는 조건
                .distinct() // 중복된 Community 제거
                .fetch(); // 결과를 리스트로 반환
    }

    @Override
    public List<Community> findLikeCommunityByUserId(Long userId) {
        QCommunity qCommunity = QCommunity.community;
        QCommunityLike qCommunityLike = QCommunityLike.communityLike;

        return jpaQueryFactory
                .selectFrom(qCommunity)
                .join(qCommunity.communityLikes,qCommunityLike)
                .where(qCommunityLike.user.id.eq(userId))
                .distinct()
                .fetch();
    }
}
