package com.springboot.peanut.data.repository.Intake;


import com.springboot.peanut.data.entity.Intake;
import com.springboot.peanut.data.repository.Intake.IntakeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntakeRepository extends JpaRepository<Intake, Long> , IntakeRepositoryCustom {
}
