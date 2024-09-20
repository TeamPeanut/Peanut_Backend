package com.springboot.peanut.data.dao.Impl;

import com.springboot.peanut.data.dao.SignDao;
import com.springboot.peanut.data.entity.User;
import com.springboot.peanut.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignDaoImpl implements SignDao {
    private final UserRepository userRepository;
    @Override
    public void saveSignUpInfo(User user) {
        userRepository.save(user);
    }
}
