package com.springboot.peanut.repository;

import com.springboot.peanut.entity.Insulin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsulinRepository extends JpaRepository<Insulin,Long> {
}
