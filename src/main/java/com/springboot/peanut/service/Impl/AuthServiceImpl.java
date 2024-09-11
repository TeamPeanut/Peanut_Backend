package com.springboot.peanut.service.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.peanut.dao.AuthDao;
import com.springboot.peanut.dto.signDto.AdditionalInfoDto;
import com.springboot.peanut.dto.signDto.KakaoResponseDto;
import com.springboot.peanut.dto.signDto.ResultDto;
import com.springboot.peanut.dto.signDto.SignInResultDto;
import com.springboot.peanut.entity.User;
import com.springboot.peanut.jwt.JwtProvider;
import com.springboot.peanut.repository.UserRepository;
import com.springboot.peanut.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthDao authDao;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final ResultStatusService resultStatusService;


    @Value("${kakao.client.id}")
    private String clientKey;

    @Value("${kakao.redirect.url}")
    private String redirectUrl;

    @Value("${kakao.accesstoken.url}")
    private String kakaoAccessTokenUrl;

    @Value("${kakao.userinfo.url}")
    private String kakaoUserInfoUrl;

    @Override
    public ResponseEntity<?> getKakaoUserInfo(String authorizeCode) {
        log.info("[kakao login] issue a authorizeCode");
        ObjectMapper objectMapper = new ObjectMapper(); //json 파싱 객체
        RestTemplate restTemplate = new RestTemplate(); //client 연결 객체

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientKey);
        params.add("redirect_uri", redirectUrl);
        params.add("code", authorizeCode);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, httpHeaders);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    kakaoAccessTokenUrl,
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class
            );
            log.info("[kakao login] authorizecode issued successfully");
            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});

            Object accessToken = responseMap.get("access_token");

            return ResponseEntity.ok(kakao_SignIn((String)accessToken));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get Kakao access token");
        }
    }

    private KakaoResponseDto getInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        HttpEntity<?> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(kakaoUserInfoUrl, HttpMethod.POST, entity, String.class);

        try {
            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
            Map<String, Object> kakaoAccount = (Map<String, Object>) responseMap.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            KakaoResponseDto responseDto = KakaoResponseDto.builder()
                    .userName((String) kakaoAccount.get("name"))
                    .phoneNumber((String) kakaoAccount.get("phone_number"))
                    .email((String) kakaoAccount.get("email"))
                    .gender((String) kakaoAccount.get("gender"))
                    .birth((String) kakaoAccount.get("birthday"))
                    .profileUrl((String) profile.get("profile_image_url"))
                    .build();

            return responseDto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public SignInResultDto kakao_SignIn(String accessToken) {
        KakaoResponseDto kakaoUserInfoResponse = getInfo(accessToken);

        SignInResultDto signInResultDto = new SignInResultDto();
        if (kakaoUserInfoResponse == null) {
            return handleSignInFailure(signInResultDto, "Failed to get Kakao user info");
        }

        User user = authDao.kakaoUserFind(kakaoUserInfoResponse.getEmail());

        if (user == null) {
            user = User.createKakaoUser(kakaoUserInfoResponse);
            authDao.KakaoUserSave(user);
            resultStatusService.setSuccess(signInResultDto);
            signInResultDto.setDetailMessage("회원가입 완료.");
        } else {
            resultStatusService.setSuccess(signInResultDto);
            signInResultDto.setDetailMessage("로그인 성공.");
        }

        signInResultDto.setToken(jwtProvider.createToken(user.getEmail(), List.of("ROLE_USER")));
        resultStatusService.setSuccess(signInResultDto);
        signInResultDto.setDetailMessage("로그인 성공.");
        log.info("[SignIn] SignInResultDto: {}", signInResultDto);

        return signInResultDto;

    }


    @Override
    public ResultDto kakao_additionalInfo(AdditionalInfoDto additionalInfoDto , HttpServletRequest request) {
        String info = jwtProvider.getUsername(request.getHeader("X-AUTH-TOKEN"));
        User user = userRepository.getByEmail(info);
        ResultDto resultDto = new ResultDto();

        if (user != null) {
            user.addKakaoAdditionalInfo(additionalInfoDto); // 기존 User 객체를 전달하여 새로운 User 객체 생성
            authDao.KakaoUserSave(user);
            resultStatusService.setSuccess(resultDto);
        } else {
            resultStatusService.setFail(resultDto);
        }
        return resultDto;
    }

    private SignInResultDto handleSignInFailure(SignInResultDto signInResultDto, String errorMessage) {
        resultStatusService.setFail(signInResultDto);
        signInResultDto.setDetailMessage(errorMessage);
        throw new RuntimeException(errorMessage);
    }
}
