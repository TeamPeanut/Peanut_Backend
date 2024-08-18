package com.springboot.peanut.service.Impl;

import com.springboot.peanut.S3.S3Uploader;
import com.springboot.peanut.dao.SignDao;
import com.springboot.peanut.dto.CommonResponse;
import com.springboot.peanut.dto.signDto.ResultDto;
import com.springboot.peanut.dto.signDto.SignInResultDto;
import com.springboot.peanut.dto.signDto.SignUpDto;
import com.springboot.peanut.entity.User;
import com.springboot.peanut.jwt.JwtProvider;
import com.springboot.peanut.repository.UserRepository;
import com.springboot.peanut.service.SignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SignServiceImpl implements SignService {
    private final JavaMailSender javaMailSender;
    private Logger logger = LoggerFactory.getLogger(SignServiceImpl.class);
    private final JwtProvider jwtProvider;
    private final SignDao signDao;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public ResultDto SignUp(SignUpDto signUpDto,HttpServletRequest request) {
        User user = (User)request.getSession().getAttribute("user");
        ResultDto resultDto = new ResultDto();
        if(user != null){
            user.setUserName(signUpDto.getName());
            user.setGender(signUpDto.getGender());
            user.setBirth(signUpDto.getBirth());
            user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
            user.setWeight(signUpDto.getWeight());
            user.setHeight(signUpDto.getHeight());
            user.setNickName(signUpDto.getNickname());
            user.setPhoneNumber(signUpDto.getPhoneNumber());
            user.setLoginMethod("Normal");
            user.setCreate_At(LocalDateTime.now());
            user.setUpdate_At(LocalDateTime.now());
            signDao.saveSignUpInfo(user);

            resultDto.setDetailMessage("회원가입 완료");
            setSuccess(resultDto);
        }else{
            resultDto.setDetailMessage("회원가입  실패");
            setFail(resultDto);
            throw new IllegalArgumentException();
        }

        return resultDto;
    }

    @Override
    public Map<String, String> sendSimpleMessage(String email, HttpServletRequest request) throws Exception {
        String ePw = createKey();

        request.getSession().setAttribute("email",email);

        MimeMessage message = createMessage(email,ePw);
        try{
            javaMailSender.send(message);
        }catch (MailException e){
            e.printStackTrace();
            throw new IllegalArgumentException();
        }

        Map<String,String>response = new HashMap<>();
        response.put("Confirmation : ", ePw);

        return response;
    }

    @Override
    public ResultDto SignIn(String email, String password) {
        // 로그인 로직
        User user = userRepository.findByEmail(email);
        logger.info("[user] : {}", user);
        logger.info("[user] : {}", user.getPassword());

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        logger.info("[getSignInResult] 패스워드 일치");

        logger.info("[getSignInResult] SignInResultDto 객체 생성");
        SignInResultDto signInResultDto = new SignInResultDto().builder()
                .token(jwtProvider.createToken(String.valueOf(user.getEmail()), user.getRoles()))
                .build();
        logger.info("[getSignInResult] SignInResultDto 객체에 값 주입");
        setSuccess(signInResultDto);
        signInResultDto.setDetailMessage("로그인 성공");
        return signInResultDto;
    }

    @Override
    public boolean verifyEmail(String confirmationCode, HttpServletRequest request) {
        String email = (String) request.getSession().getAttribute("email");
        User user = new User();
        if(email != null){
            user.setEmail(email);
            user.setVerified(true);
            request.getSession().setAttribute("user",user);

            return true;
        }
        return false;
    }
    private MimeMessage createMessage(String to, String ePw) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);
        message.setSubject("이메일 인증");
        String msgg = "";
        msgg += "<div style='margin:20px;'>";
        msgg += "<h1> OVER-DOSE </h1>";
        msgg += "<br>";
        msgg += "<p>인증번호 입니다.</p>";
        msgg += "<br>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += ePw + "</strong><div><br/> ";
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("nankys0510@naver.com", "OverDose"));//보내는 사람

        return message;
    }
    public static String createKey(){
        int number = (int)(Math.random()*90000)+100000;
        return String.valueOf(number);
    }

    private void setSuccess(ResultDto resultDto) {
        resultDto.setSuccess(true);
        resultDto.setCode(CommonResponse.SUCCESS.getCode());
        resultDto.setMsg(CommonResponse.SUCCESS.getMsg());

    }

    private void setFail(ResultDto resultDto) {
        resultDto.setSuccess(false);
        resultDto.setCode(CommonResponse.Fail.getCode());
        resultDto.setMsg(CommonResponse.Fail.getMsg());
    }
}
