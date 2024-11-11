package com.springboot.peanut.service.User;

import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.data.dto.user.*;
import com.springboot.peanut.data.dto.user.Requset.UpdateUserInfoDto;
import com.springboot.peanut.data.dto.user.Requset.UserAlamInfoRequestDto;
import com.springboot.peanut.data.dto.user.Requset.UpdateUserAddInfoDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UserService {
    ResultDto updateAdditionalUserInfo(UpdateUserAddInfoDto updateUserAddInfoDto, MultipartFile image, HttpServletRequest request) throws IOException;
    ResultDto updateUserInfo(UpdateUserInfoDto updateUserInfoDto, HttpServletRequest request) throws IOException;
    Map<String, String> sendInviteCode(HttpServletRequest request) throws Exception;
    ResultDto confirmGuardianRelation(String confirmationCode, HttpServletRequest request);
    PatientConnectingResponse getPatientConnectingInfo(String email , HttpServletRequest request);
    List<GetCommunityByUserDto> getCreateCommunityByUser(HttpServletRequest request);
    List<GetCommunityByUserDto>getCommentCommunityByUser(HttpServletRequest request);
    List<GetCommunityByUserDto>getLikeCommunityByUser(HttpServletRequest request);
    UserAlamInfoRequestDto saveUserAlamInfo(UserAlamInfoRequestDto alamInfoDto, HttpServletRequest request);
    GetPatientResponseDto getPatientInfo(HttpServletRequest request);
    GetPatientResponseDto getGuardianInfo(HttpServletRequest request);
    List<GetConnectingInfoDto> getConnectingInfo(HttpServletRequest request);
    GetUserInfoMyPage getUserInfoMyPage(HttpServletRequest request);
    UserAlamInfoResponseDto getUserAlamInfo(HttpServletRequest request);
}
