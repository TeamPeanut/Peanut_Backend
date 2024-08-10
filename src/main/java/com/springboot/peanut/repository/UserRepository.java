package com.springboot.peanut.repository;

import com.springboot.peanut.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
    User getByEmail(String email);
}
