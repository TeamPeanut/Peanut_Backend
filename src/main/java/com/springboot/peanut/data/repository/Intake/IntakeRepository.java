package com.springboot.peanut.data.repository.Intake;


import com.springboot.peanut.data.entity.Insulin;
import com.springboot.peanut.data.entity.Intake;
import com.springboot.peanut.data.repository.Intake.IntakeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IntakeRepository extends JpaRepository<Intake, Long> , IntakeRepositoryCustom {
    @Query("SELECT i FROM Intake i JOIN FETCH i.intakeTime WHERE i.user.id = :userId")
    List<Intake> findAllByUserId(@Param("userId") Long userId);
}
