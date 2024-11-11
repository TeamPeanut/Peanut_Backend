package com.springboot.peanut.data.dao;

import com.springboot.peanut.data.dto.user.*;
import com.springboot.peanut.data.dto.user.Requset.UpdateUserInfoDto;
import com.springboot.peanut.data.dto.user.Requset.UserAlamInfoRequestDto;
import com.springboot.peanut.data.entity.User;

import java.util.List;
import java.util.Optional;


public interface UserDao {
    void updateUserAdditionalInfo(UserUpdateResponseDto userUpdateResponseDto);
    void updateUserInfo(Long userId, UpdateUserInfoDto updateUserInfoDto);
    User save(User user);
    PatientConnectingResponse findPatientConnecting(String email);
    Optional<User> findUserByEmail(String email);
    GetPatientResponseDto findPatientByGuardian(Long id);
    GetPatientResponseDto findGuardianByPatient(Long id);
    List<GetConnectingInfoDto> findConnectingInfo(String email);
    List<User> findAllUser();
    void saveUserAlamInfo(Long userId,UserAlamInfoRequestDto userAlamInfoRequestDto);

}
