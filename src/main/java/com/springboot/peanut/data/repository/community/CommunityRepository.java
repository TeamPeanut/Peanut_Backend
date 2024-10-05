package com.springboot.peanut.data.repository.community;

import com.springboot.peanut.data.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityRepository extends JpaRepository<Community,Long>,CommunityCustomRepository{
    List<Community> findAll();
}
