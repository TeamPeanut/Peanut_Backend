package com.springboot.peanut.data.repository;

import com.springboot.peanut.data.entity.PatientGuardian;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientGuardianRepository extends JpaRepository<PatientGuardian, Long> {
}
