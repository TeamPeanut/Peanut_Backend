package com.springboot.peanut.data.repository;

import com.springboot.peanut.data.dto.user.UserUpdateResponseDto;
import com.springboot.peanut.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
    User getByEmail(String email);

}
