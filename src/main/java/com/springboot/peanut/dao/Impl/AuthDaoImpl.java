package com.springboot.peanut.dao.Impl;

import com.springboot.peanut.dao.AuthDao;
import com.springboot.peanut.entity.User;
import com.springboot.peanut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthDaoImpl implements AuthDao {
    private final UserRepository userRepository;

    @Override
    public User KakaoUserSave(User user) {
        return userRepository.save(user);
    }

    @Override
    public User kakaoUserFind(String email) {
        return userRepository.findByEmail(email);
    }


}
