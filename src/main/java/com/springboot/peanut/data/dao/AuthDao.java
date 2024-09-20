package com.springboot.peanut.data.dao;


import com.springboot.peanut.data.entity.User;

public interface AuthDao {

    User KakaoUserSave(User user);
    User kakaoUserFind(String email);
}
