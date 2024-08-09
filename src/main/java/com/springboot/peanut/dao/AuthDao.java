package com.springboot.peanut.dao;

import com.springboot.peanut.entity.User;

public interface AuthDao {

    User KakaoUserSave(User user);
    User kakaoUserFind(String email);
}
