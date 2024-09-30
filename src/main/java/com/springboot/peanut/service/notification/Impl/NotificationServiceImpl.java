package com.springboot.peanut.service.notification.Impl;

import com.springboot.peanut.data.dao.NotificationDao;
import com.springboot.peanut.data.dao.UserDao;
import com.springboot.peanut.data.entity.Notification;
import com.springboot.peanut.data.entity.PatientGuardian;
import com.springboot.peanut.data.entity.User;
import com.springboot.peanut.data.repository.PatientGuardianRepository;
import com.springboot.peanut.data.repository.UserRepository;
import com.springboot.peanut.jwt.JwtAuthenticationService;
import com.springboot.peanut.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationDao notificationDao;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final PatientGuardianRepository patientGuardianRepository;

    @Override
    public void sendMedicationNotification(HttpServletRequest request , String status) {
        Optional<User> patient = jwtAuthenticationService.authenticationToken(request);
        Optional<User> guardian = userRepository.findById(patient.get().getId());

        if(guardian.isPresent()) {
            String message = status.equals("TAKEN")?
            String.format("%s님이 약을 섭취했습니다.", patient.get().getUsername()):
                    String.format("%s님이 약을 섭취하지 않았습니다.", guardian.get().getUsername());
            log.info("환자: {}, 보호자에게 전송할 메시지: {}", patient.get().getUsername(), message);

           Notification notification =  Notification.createNotification(patient.get(),guardian.get(),message,status);

            notificationDao.save(notification);
            simpMessagingTemplate.convertAndSend("/topic/caregiver/medication",message);

        }

    }

    @Override
    public void remindPatient(HttpServletRequest request, String status) {
        Optional<User> guardian = jwtAuthenticationService.authenticationToken(request);

        if (guardian.isPresent()) {
            List<PatientGuardian> guardiansPatient = patientGuardianRepository.findByGuardianIdAndVerified(guardian.get().getId(), true);     log.info("[guardiansPatient] : {}" , guardiansPatient);
            log.info("Guardian ID: {}", guardian.get().getId());

            List<User> patients = guardiansPatient.stream()
                    .map(PatientGuardian::getPatient)
                    .collect(Collectors.toList());

            log.info("[patient] : {}" , patients);
            log.info("보호자: {}, 연결된 환자 수: {}", guardian.get().getUsername(), guardiansPatient.size());
            patients.forEach(patient -> {
                String message = String.format("%s님이 메시지를 보냈습니다. 약 섭취 하세요.", patient.getUsername());

                log.info("보호자: {}, 환자에게 전송할 메시지: {}", patient.getUsername(), message);

                // 알림 생성
                Notification notification = Notification.createNotification(patient, guardian.get(), message, "SENT");

                notificationDao.save(notification);
                // WebSocket을 통해 환자에게 알림 전송
                simpMessagingTemplate.convertAndSend("/queue/patient/remind", message);
            });
        }
    }
}