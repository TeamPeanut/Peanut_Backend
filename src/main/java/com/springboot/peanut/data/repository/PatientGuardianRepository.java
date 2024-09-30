package com.springboot.peanut.data.repository;

import com.springboot.peanut.data.entity.PatientGuardian;
import com.springboot.peanut.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientGuardianRepository extends JpaRepository<PatientGuardian, Long> {

    List<PatientGuardian> findByGuardianIdAndVerified(Long guardianId, boolean verified);
}
