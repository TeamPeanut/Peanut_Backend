package com.springboot.peanut.data.repository.Insulin;

import com.springboot.peanut.data.entity.Insulin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InsulinRepository extends JpaRepository<Insulin,Long>,InsulinRepositoryCustom{
    // InsulinRepository에 추가
    @Query("SELECT i FROM Insulin i JOIN FETCH i.administrationTime WHERE i.user.id = :userId")
    Optional<Insulin> findByUserId(@Param("userId") Long userId);



}
