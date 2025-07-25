package com.springboot.peanut.data.repository.community.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.peanut.data.entity.*;
import com.springboot.peanut.data.repository.community.CommunityCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunityCustomRepositoryImpl implements CommunityCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<List<Community>> findCreateCommunityById(Long id) {
        QCommunity community = QCommunity.community;
        QUser quser = QUser.user;

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(community)
                .leftJoin(community.user,quser).fetchJoin()
                .where(community.user.id.eq(id))
                .fetch());
    }

    @Override
    public List<Community> findCommentCommunityByUserId(Long userId) {
        QCommunity qCommunity = QCommunity.community;
        QComment qComment = QComment.comment;

        return jpaQueryFactory
                .selectFrom(qCommunity)
                .leftJoin(qCommunity.comments, qComment).fetchJoin() // Fetch Join 추가
                .leftJoin(qCommunity.user).fetchJoin() // Fetch Join 추가
                .where(qComment.user.id.eq(userId)) // Comment의 user.id와 일치
                .distinct() // 중복 제거
                .fetch();
    }

    @Override
    public List<Community> findLikeCommunityByUserId(Long userId) {
        QCommunity qCommunity = QCommunity.community;
        QCommunityLike qCommunityLike = QCommunityLike.communityLike;

        return jpaQueryFactory
                .selectFrom(qCommunity)
                .leftJoin(qCommunity.communityLikes, qCommunityLike).fetchJoin() // Fetch Join 추가
                .leftJoin(qCommunity.user).fetchJoin() // Fetch Join 추가
                .where(qCommunityLike.user.id.eq(userId))
                .distinct() // 중복 제거
                .fetch();
    }

    @Override
    public List<Community> findCommunityBySearch(Long userId, String search) {
        QCommunity qCommunity = QCommunity.community;

        return jpaQueryFactory
                .selectFrom(qCommunity)
                .leftJoin(qCommunity.user).fetchJoin() // Fetch Join 추가
                .where(qCommunity.user.id.eq(userId)
                        .and(qCommunity.title.contains(search)))
                .fetch();
    }
}