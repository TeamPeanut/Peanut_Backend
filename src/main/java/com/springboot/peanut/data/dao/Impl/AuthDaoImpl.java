package com.springboot.peanut.data.dao.Impl;

import com.springboot.peanut.data.dao.AuthDao;
import com.springboot.peanut.data.entity.User;
import com.springboot.peanut.data.repository.UserRepository;
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
