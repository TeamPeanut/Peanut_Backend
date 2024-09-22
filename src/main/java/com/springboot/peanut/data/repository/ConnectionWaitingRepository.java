package com.springboot.peanut.data.repository;

import com.springboot.peanut.data.entity.ConnectionWaiting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConnectionWaitingRepository extends JpaRepository<ConnectionWaiting, Long> {
Optional<ConnectionWaiting> findByPatientEmailAndInviteCode (String patientEmail, String inviteCode);
}
