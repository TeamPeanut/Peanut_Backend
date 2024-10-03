package com.springboot.peanut.service.notification.Impl;

import com.springboot.peanut.data.dao.NotificationDao;
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

    /**
     * 환자가 약을 섭취했거나 섭취하지 않았을 때 보호자에게 알림을 전송하는 메소드.
     */
    @Override
    public void sendMedicationNotification(HttpServletRequest request, String status) {
        Optional<User> patient = jwtAuthenticationService.authenticationToken(request);
        if (patient.isPresent()) {
            Optional<User> guardian = userRepository.findById(patient.get().getId());

            if (guardian.isPresent()) {
                String message = status.equals("TAKEN")
                        ? String.format("%s님이 약을 섭취했습니다.", patient.get().getUserName())
                        : String.format("%s님이 약을 섭취하지 않았습니다.", patient.get().getUserName());

                log.info("환자: {}, 보호자에게 전송할 메시지: {}", patient.get().getUserName(), message);

                Notification notification = Notification.createNotification(patient.get(), guardian.get(), message, status);
                notificationDao.save(notification);

                // 보호자에게 메시지를 전송
                simpMessagingTemplate.convertAndSendToUser(guardian.get().getUserName(), "/queue/caregiver/medication", message);
                log.info("전송된 메시지: {}", message); // 추가 로그
            }
        }
    }

    /**
     * 보호자가 환자에게 약 복용을 상기시키는 알림을 전송하는 메소드.
     */
    @Override
    public void remindPatient(HttpServletRequest request, String status) {
        Optional<User> guardian = jwtAuthenticationService.authenticationToken(request);

        if (guardian.isPresent()) {
            // 보호자에 연결된 환자 목록 조회
            List<PatientGuardian> guardiansPatient = patientGuardianRepository.findByGuardianIdAndVerified(guardian.get().getId(), true);
            log.info("[guardiansPatient] : {}", guardiansPatient);
            log.info("Guardian ID: {}", guardian.get().getId());

            List<User> patients = guardiansPatient.stream()
                    .map(PatientGuardian::getPatient)
                    .collect(Collectors.toList());

            log.info("[patient] : {}", patients);
            log.info("보호자: {}, 연결된 환자 수: {}", guardian.get().getUserName(), guardiansPatient.size());

            // 각 환자에게 알림 전송
            patients.forEach(patient -> {
                String message = String.format("%s님이 메시지를 보냈습니다. 약 섭취 하세요.", guardian.get().getUserName());
                log.info("보호자: {}, 환자에게 전송할 메시지: {}", patient.getUserName(), message);

                // 알림 생성 및 저장
                Notification notification = Notification.createNotification(patient, guardian.get(), message, "SENT");
                notificationDao.save(notification);

                // WebSocket을 통해 환자에게 알림 전송
                simpMessagingTemplate.convertAndSendToUser(patient.getUserName(), "/queue/patient/remind", message);
                log.info("전송된 메시지: {}", message); // 추가 로그
            });
        }
    }
}