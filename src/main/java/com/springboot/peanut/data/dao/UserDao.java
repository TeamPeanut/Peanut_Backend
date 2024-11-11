package com.springboot.peanut.data.dao;

import com.springboot.peanut.data.dto.user.*;
import com.springboot.peanut.data.entity.PatientGuardian;
import com.springboot.peanut.data.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


public interface UserDao {
    void updateUser(UserUpdateResponseDto userUpdateResponseDto);
    User save(User user);
    PatientConnectingResponse findPatientConnecting(String email);
    Optional<User> findUserByEmail(String email);
    GetPatientResponseDto findPatientByGuardian(Long id);
    GetPatientResponseDto findGuardianByPatient(Long id);
    List<GetConnectingInfoDto> findConnectingInfo(String email);
    List<User> findAllUser();
    void saveUserAlamInfo(UserAlamInfoRequestDto userAlamInfoRequestDto);

}
