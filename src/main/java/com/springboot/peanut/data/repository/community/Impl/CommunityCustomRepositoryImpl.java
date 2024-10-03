package com.springboot.peanut.data.repository.community.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.peanut.data.entity.Community;
import com.springboot.peanut.data.entity.QCommunity;
import com.springboot.peanut.data.repository.community.community.CommunityCustomRepository;
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
}
