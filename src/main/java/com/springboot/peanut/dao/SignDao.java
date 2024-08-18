package com.springboot.peanut.dao;

import com.springboot.peanut.dto.signDto.SignUpDto;
import com.springboot.peanut.entity.User;

public interface SignDao {
    void saveSignUpInfo(User user);
}
