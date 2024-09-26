package com.springboot.peanut.data.dao.Impl;

import com.springboot.peanut.data.dao.UserDao;
import com.springboot.peanut.data.dto.user.PatientConnectingResponse;
import com.springboot.peanut.data.dto.user.UserUpdateResponseDto;
import com.springboot.peanut.data.entity.User;
import com.springboot.peanut.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void updateUser(UserUpdateResponseDto userUpdateResponseDto) {
        // 기존 사용자 정보 불러오기
        User existingUser = userRepository.findById(userUpdateResponseDto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

        // null이 아닌 필드만 업데이트
        if (userUpdateResponseDto.getNickName() != null) {
            existingUser.setNickName(userUpdateResponseDto.getNickName());
        }
        if (userUpdateResponseDto.getWeight() != null) {
            existingUser.setWeight(userUpdateResponseDto.getWeight());
        }
        if (userUpdateResponseDto.getHeight() != null) {
            existingUser.setHeight(userUpdateResponseDto.getHeight());
        }
        if (userUpdateResponseDto.getImageUrl() != null) {
            existingUser.setProfileUrl(userUpdateResponseDto.getImageUrl());
        }

        // 저장
        userRepository.save(existingUser);
    }

    @Override
    public PatientConnectingResponse findPatientConnecting(String email) {
        Optional<User> patient = userRepository.findByEmail(email);
        PatientConnectingResponse patientConnectingResponse = new PatientConnectingResponse(
                patient.get().getUserName(),
                patient.get().getGender(),
                patient.get().getPhoneNumber(),
                patient.get().getBirth(),
                patient.get().getProfileUrl()
        );

        return patientConnectingResponse;
    }
}
