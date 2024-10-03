package com.springboot.peanut.data.repository.community.community;

import com.springboot.peanut.data.entity.Community;

import java.util.List;
import java.util.Optional;

public interface CommunityCustomRepository {
    Optional<List<Community>> findCreateCommunityById(Long id);
}
