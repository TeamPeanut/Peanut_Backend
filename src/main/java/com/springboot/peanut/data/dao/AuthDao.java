package com.springboot.peanut.data.dao;


import com.springboot.peanut.data.entity.User;

import java.util.Optional;

public interface AuthDao {

    User KakaoUserSave(User user);
    Optional<User> kakaoUserFind(String email);
}
