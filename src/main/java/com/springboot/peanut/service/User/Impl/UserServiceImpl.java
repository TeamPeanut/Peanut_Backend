package com.springboot.peanut.service.User.Impl;

import com.springboot.peanut.S3.S3Uploader;
import com.springboot.peanut.data.dao.ConnectionWaitngDao;
import com.springboot.peanut.data.dao.PatientGuardianDao;
import com.springboot.peanut.data.dao.UserDao;
import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.data.dto.user.UserUpdateRequestDto;
import com.springboot.peanut.data.dto.user.UserUpdateResponseDto;
import com.springboot.peanut.data.entity.ConnectionWaiting;
import com.springboot.peanut.data.entity.PatientGuardian;
import com.springboot.peanut.data.entity.User;
import com.springboot.peanut.data.repository.ConnectionWaitingRepository;
import com.springboot.peanut.data.repository.UserRepository;
import com.springboot.peanut.jwt.JwtAuthenticationService;
import com.springboot.peanut.service.Result.ResultStatusService;
import com.springboot.peanut.service.User.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final S3Uploader s3Uploader;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final ResultStatusService resultStatusService;
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final ConnectionWaitngDao connectionWaitngDao;
    private final ConnectionWaitingRepository connectionWaitingRepository;
    private final PatientGuardianDao patientGuardianDao;

    @Override
    public ResultDto updateAdditionalUserInfo(UserUpdateRequestDto userUpdateRequestDto, MultipartFile image, HttpServletRequest request) throws IOException {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);
        log.info("[userEmail] : {}",user.get().getEmail());
        ResultDto resultDto = new ResultDto();

        if(user != null) {

            String imageUrl = s3Uploader.uploadImage(image, "peanut");
            UserUpdateResponseDto userUpdateResponseDto = new UserUpdateResponseDto(
                    user.get().getId(),
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

    @Override
    public Map<String, String> sendInviteCode(String email, HttpServletRequest request) throws Exception {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);
        Optional<User> patient = userRepository.findByEmail(email);
        String ePw = make_InviteCode();
        request.getSession().setAttribute("email", email);

        MimeMessage message = createMessage(user.get().getUserName(),email,ePw);
        try{
            javaMailSender.send(message);
            ConnectionWaiting saveConnectionWaiting = new ConnectionWaiting(
                    patient.get().getUserName(),
                    patient.get().getEmail(),
                    ePw,
                    "승인",
                    user.get().getEmail()
            );
            connectionWaitngDao.save(saveConnectionWaiting);
        }catch (MailException e){
            e.printStackTrace();
            throw new IllegalArgumentException();
        }

        Map<String,String>response = new HashMap<>();
        response.put("GuardianName",user.get().getUserName());
        response.put("Confirmation : ", ePw);

        return response;
    }

    @Override
    public ResultDto confirmGuardianRelation(String inviteCode, HttpServletRequest request) {
        ResultDto resultDto = new ResultDto();
        String patientEmail = (String) request.getSession().getAttribute("email");

        // ConnectionWaiting 엔티티에서 환자의 이메일과 초대 코드를 사용하여 정보 조회
        Optional<ConnectionWaiting> connectionWaitingOpt = connectionWaitingRepository.findByPatientEmailAndInviteCode(patientEmail, inviteCode);

        if (!connectionWaitingOpt.isPresent()) {
            return createFailureResult(resultDto, "인증 코드가 올바르지 않습니다.");
        }

        ConnectionWaiting connectionWaiting = connectionWaitingOpt.get();

        // 환자 정보 조회
        Optional<User> patientOpt = userRepository.findByEmail(patientEmail);

        if (!patientOpt.isPresent()) {
            return createFailureResult(resultDto, "환자를 찾을 수 없습니다.");
        }

        User patient = patientOpt.get();

        // 보호자 이메일을 통해 보호자 정보 조회
        Optional<User> guardianOpt = userRepository.findByEmail(connectionWaiting.getGuardianEmail());

        if (!guardianOpt.isPresent()) {
            return createFailureResult(resultDto, "보호자를 찾을 수 없습니다.");
        }

        User guardian = guardianOpt.get();

        // PatientGuardian 엔티티에 보호자-환자 관계 저장
        PatientGuardian patientGuardian = new PatientGuardian(
                patient, guardian, true);
        patientGuardianDao.save(patientGuardian);

        resultDto.setDetailMessage("보호자와 환자의 관계가 성공적으로 연결되었습니다.");
        resultDto.setSuccess(true);

        return resultDto;
    }

    private ResultDto createFailureResult(ResultDto resultDto, String message) {
        resultDto.setDetailMessage(message);
        resultDto.setSuccess(false);
        return resultDto;
    }



    private MimeMessage createMessage(String name, String email, String ePw) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, email);
        message.setSubject("환자 등록 인증 코드");
        String msgg = "";
        msgg += "<div style='margin:20px;'>";
        msgg += "<h1> 보호자 등록  </h1>";
        msgg += "<br>";
        msgg += name + "님이 보호자로 환자 등록 확인 코드를 보냈습니다.";
        msgg += "<br>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>환자 등록 확인 인증코드 입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += ePw + "</strong><div><br/> ";
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("nankys0510@naver.com", "OverDose"));//보내는 사람

        return message;
    }
    private String make_InviteCode(){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 7;
        Random random = new Random();

        String randomNum = random.ints(leftLimit,rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return randomNum;
    }



}
