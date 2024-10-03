package com.springboot.peanut.data.repository.community.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.peanut.data.entity.Community;
import com.springboot.peanut.data.entity.QCommunity;
import com.springboot.peanut.data.entity.QCommunityLike;
import com.springboot.peanut.data.repository.community.like.LikeCustomRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeCustomRepositoryImpl implements LikeCustomRepository {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<List<Community>> findLikeCommunityById(Long userId) {
        QCommunity qCommunity = QCommunity.community;
        QCommunityLike qCommunityLike = QCommunityLike.communityLike;

        return Optional.ofNullable(jpaQueryFactory
                .select(qCommunity)
                .from(qCommunity)
                .join(qCommunity.communityLikes, qCommunityLike) // 댓글을 가진 커뮤니티 선택
                .where(qCommunityLike.user.id.eq(userId)) // 사용자가 작성한 댓글로 필터링
                .fetch());
    }
}
