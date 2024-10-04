package com.springboot.peanut.data.dao;

import com.springboot.peanut.data.dto.user.PatientConnectingResponse;
import com.springboot.peanut.data.dto.user.UserUpdateResponseDto;
import com.springboot.peanut.data.entity.User;

import java.util.Optional;


public interface UserDao {
    void updateUser(UserUpdateResponseDto userUpdateResponseDto);
    User save(User user);
    PatientConnectingResponse findPatientConnecting(String email);
    Optional<User> findUserByEmail(String email);

}
