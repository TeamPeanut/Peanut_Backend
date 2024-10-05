package com.springboot.peanut.data.dao.Impl;

import com.springboot.peanut.data.dao.UserDao;
import com.springboot.peanut.data.dto.user.GetPatientResponseDto;
import com.springboot.peanut.data.dto.user.PatientConnectingResponse;
import com.springboot.peanut.data.dto.user.UserUpdateResponseDto;
import com.springboot.peanut.data.entity.PatientGuardian;
import com.springboot.peanut.data.entity.User;
import com.springboot.peanut.data.repository.PatientGuardianRepository;
import com.springboot.peanut.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDaoImpl implements UserDao {

    private final UserRepository userRepository;
    private final PatientGuardianRepository patientGuardianRepository;

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
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

    @Override
    public List<GetPatientResponseDto> findPatientByGuardian(Long id) {
        List<GetPatientResponseDto> getPatientResponseDtoList = new ArrayList<>();
        List<PatientGuardian> guardiansPatient = patientGuardianRepository.findByGuardianId(id);
        log.info("[guardiansPatient] : {}", guardiansPatient);

        List<User> patients = guardiansPatient.stream()
                .map(PatientGuardian::getPatient)
                .collect(Collectors.toList());

        for(User user: patients) {
            GetPatientResponseDto getPatientResponseDto = new GetPatientResponseDto(
                    user.getId(),
                    user.getUserName(),
                    user.getGender(),
                    user.getBirth(),
                    user.getHeight(),
                    user.getWeight(),
                    user.getProfileUrl()
            );
        getPatientResponseDtoList.add(getPatientResponseDto);
        }

        return getPatientResponseDtoList;
    }
}
