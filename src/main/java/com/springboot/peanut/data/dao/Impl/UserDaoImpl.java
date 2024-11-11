package com.springboot.peanut.data.dao.Impl;

import com.springboot.peanut.data.dao.UserDao;
import com.springboot.peanut.data.dto.user.*;
import com.springboot.peanut.data.dto.user.Requset.UpdateUserInfoDto;
import com.springboot.peanut.data.dto.user.Requset.UserAlamInfoRequestDto;
import com.springboot.peanut.data.entity.ConnectionWaiting;
import com.springboot.peanut.data.entity.PatientGuardian;
import com.springboot.peanut.data.entity.User;
import com.springboot.peanut.data.repository.ConnectionWaitingRepository;
import com.springboot.peanut.data.repository.PatientGuardianRepository;
import com.springboot.peanut.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDaoImpl implements UserDao {

    private final UserRepository userRepository;
    private final PatientGuardianRepository patientGuardianRepository;
    private final ConnectionWaitingRepository connectionWaitingRepository;

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void updateUserAdditionalInfo(UserUpdateResponseDto userUpdateResponseDto) {
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
    public void updateUserInfo(Long userId, UpdateUserInfoDto updateUserInfoDto) {
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if(updateUserInfoDto.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(updateUserInfoDto.getPhoneNumber());
        }
        if (updateUserInfoDto.getGender() != null) {
            existingUser.setGender(updateUserInfoDto.getGender());
        }
        if(updateUserInfoDto.getBirthday() != null) {
            existingUser.setBirth(updateUserInfoDto.getBirthday());
        }
        if(updateUserInfoDto.getUserName() != null) {
            existingUser.setUserName(updateUserInfoDto.getUserName());
        }
        if (updateUserInfoDto.getPassword() != null) {
            existingUser.setPassword(updateUserInfoDto.getPassword());
        }
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
    public GetPatientResponseDto findPatientByGuardian(Long id) {
        PatientGuardian guardiansPatient = patientGuardianRepository.findByGuardianId(id);
        log.info("[guardiansPatient] : {}", guardiansPatient);
        if (guardiansPatient == null) {
            return null;
        }
        User patients = guardiansPatient.getPatient();

            GetPatientResponseDto getPatientResponseDto = new GetPatientResponseDto(
                    patients.getId(),
                    patients.getUserName(),
                    patients.getGender(),
                    patients.getBirth(),
                    patients.getHeight(),
                    patients.getWeight(),
                    patients.getProfileUrl()
            );

        return getPatientResponseDto;
    }

    @Override
    public GetPatientResponseDto findGuardianByPatient(Long id) {
        PatientGuardian guardiansPatient = patientGuardianRepository.findByPatientId(id);
        log.info("[guardiansPatient] : {}", guardiansPatient);

        if (guardiansPatient == null) {
            return null;
        }
        User guardian = guardiansPatient.getGuardian();

        GetPatientResponseDto getPatientResponseDto = new GetPatientResponseDto(
                guardian.getId(),
                guardian.getUserName(),
                guardian.getGender(),
                guardian.getBirth(),
                guardian.getHeight(),
                guardian.getWeight(),
                guardian.getProfileUrl()
        );

        return getPatientResponseDto;
    }

    @Override
    public List<GetConnectingInfoDto> findConnectingInfo(String email) {
        List<GetConnectingInfoDto> getConnectingInfoDtos = new ArrayList<>();
        List<ConnectionWaiting> connectionWaitingList = connectionWaitingRepository.findByGuardianEmail(email);

        for(ConnectionWaiting connectionWaiting: connectionWaitingList) {
            Long id = connectionWaiting.getId();
            String userName = connectionWaiting.getPatientName();
            String status = connectionWaiting.getStatus();

            GetConnectingInfoDto getConnectingInfoDto = new GetConnectingInfoDto(
                    id,userName,status
            );
            getConnectingInfoDtos.add(getConnectingInfoDto);
        }

        return getConnectingInfoDtos;
    }

    @Override
    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    @Override
    public void saveUserAlamInfo(Long userId,UserAlamInfoRequestDto userAlamInfoRequestDto) {

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setGuardianAlam(userAlamInfoRequestDto.isGuardianAlam());
        user.setMedicationAlam(userAlamInfoRequestDto.isMedicationAlam());
        user.setInsulinAlam(userAlamInfoRequestDto.isInsulinAlam());
        userRepository.save(user);
    }
}
