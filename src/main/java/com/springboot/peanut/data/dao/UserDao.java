package com.springboot.peanut.data.dao;

import com.springboot.peanut.data.dto.user.GetPatientResponseDto;
import com.springboot.peanut.data.dto.user.PatientConnectingResponse;
import com.springboot.peanut.data.dto.user.UserUpdateResponseDto;
import com.springboot.peanut.data.entity.PatientGuardian;
import com.springboot.peanut.data.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public interface UserDao {
    void updateUser(UserUpdateResponseDto userUpdateResponseDto);
    User save(User user);
    PatientConnectingResponse findPatientConnecting(String email);
    Optional<User> findUserByEmail(String email);
    List<GetPatientResponseDto> findPatientByGuardian(Long id);


}
