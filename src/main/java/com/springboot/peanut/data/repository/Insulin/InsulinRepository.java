package com.springboot.peanut.data.repository.Insulin;

import com.springboot.peanut.data.entity.Insulin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InsulinRepository extends JpaRepository<Insulin,Long>,InsulinRepositoryCustom{
Optional<Insulin> findByUserId(Long userId);
}
