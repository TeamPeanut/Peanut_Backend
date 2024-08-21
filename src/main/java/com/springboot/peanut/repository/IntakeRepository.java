package com.springboot.peanut.repository;

import com.springboot.peanut.entity.Intake;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntakeRepository extends JpaRepository<Intake,Long> {
}
