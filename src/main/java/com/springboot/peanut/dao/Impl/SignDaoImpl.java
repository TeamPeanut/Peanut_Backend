package com.springboot.peanut.dao.Impl;

import com.springboot.peanut.dao.SignDao;
import com.springboot.peanut.dto.signDto.SignUpDto;
import com.springboot.peanut.entity.User;
import com.springboot.peanut.repository.UserRepository;
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
