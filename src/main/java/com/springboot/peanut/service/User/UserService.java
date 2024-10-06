package com.springboot.peanut.service.User;

import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.data.dto.user.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UserService {
    ResultDto updateAdditionalUserInfo(UserUpdateRequestDto userUpdateRequestDto, MultipartFile image, HttpServletRequest request) throws IOException;
    Map<String, String> sendInviteCode(HttpServletRequest request) throws Exception;
    ResultDto confirmGuardianRelation(String confirmationCode, HttpServletRequest request);
    PatientConnectingResponse getPatientConnectingInfo(String email , HttpServletRequest request);
    List<GetCommunityByUserDto> getCreateCommunityByUser(HttpServletRequest request);
    List<GetCommunityByUserDto>getCommentCommunityByUser(HttpServletRequest request);
    List<GetCommunityByUserDto>getLikeCommunityByUser(HttpServletRequest request);
    UserAlamInfoDto UserAlamInfo(UserAlamInfoDto alamInfoDto,HttpServletRequest request);
    List<GetPatientResponseDto> getPatientInfo(HttpServletRequest request);
    GetUserInfoMyPage getUserInfoMyPage(HttpServletRequest request);
}
