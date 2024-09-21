package com.springboot.peanut.data.dao;

import com.springboot.peanut.data.dto.user.UserUpdateResponseDto;


public interface UserDao {
    void updateUser(UserUpdateResponseDto userUpdateResponseDto);


}
