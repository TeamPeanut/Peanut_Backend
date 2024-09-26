package com.springboot.peanut.service.User;

import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.data.dto.user.PatientConnectingResponse;
import com.springboot.peanut.data.dto.user.UserUpdateRequestDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

public interface UserService {
    ResultDto updateAdditionalUserInfo(UserUpdateRequestDto userUpdateRequestDto, MultipartFile image, HttpServletRequest request) throws IOException;
    Map<String, String> sendInviteCode(HttpServletRequest request) throws Exception;
    ResultDto confirmGuardianRelation(String confirmationCode, HttpServletRequest request);
    PatientConnectingResponse getPatientConnectingInfo(String email , HttpServletRequest request);


}
