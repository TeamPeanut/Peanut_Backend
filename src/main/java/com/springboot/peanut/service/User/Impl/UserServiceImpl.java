package com.springboot.peanut.service.User.Impl;

import com.springboot.peanut.S3.S3Uploader;
import com.springboot.peanut.data.dao.UserDao;
import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.data.dto.user.UserUpdateRequestDto;
import com.springboot.peanut.data.dto.user.UserUpdateResponseDto;
import com.springboot.peanut.data.entity.User;
import com.springboot.peanut.jwt.JwtAuthenticationService;
import com.springboot.peanut.service.Result.ResultStatusService;
import com.springboot.peanut.service.User.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final S3Uploader s3Uploader;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final ResultStatusService resultStatusService;

    @Override
    public ResultDto updateAdditionalUserInfo(UserUpdateRequestDto userUpdateRequestDto, MultipartFile image, HttpServletRequest request) throws IOException {
        User user = jwtAuthenticationService.authenticationToken(request);
        log.info("[userEmail] : {}",user.getEmail());
        ResultDto resultDto = new ResultDto();

        if(user != null) {

            String imageUrl = s3Uploader.uploadImage(image, "peanut");
            UserUpdateResponseDto userUpdateResponseDto = new UserUpdateResponseDto(
                    user.getId(),
                    userUpdateRequestDto.getNickName(),
                    userUpdateRequestDto.getWeight(),
                    userUpdateRequestDto.getHeight(),
                    imageUrl
            );
            userDao.updateUser(userUpdateResponseDto);
            resultDto.setDetailMessage("회원 정보 수정 완료.");
            resultStatusService.setSuccess(resultDto);

        }
        return resultDto;
    }
}
