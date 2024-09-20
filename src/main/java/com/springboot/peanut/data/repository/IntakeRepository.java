package com.springboot.peanut.data.repository;

import com.springboot.peanut.data.entity.Intake;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntakeRepository extends JpaRepository<Intake,Long> {
}
