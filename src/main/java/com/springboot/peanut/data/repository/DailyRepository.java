package com.springboot.peanut.data.repository;

import com.springboot.peanut.data.entity.DailyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyRepository extends JpaRepository<DailyStatus,Long> {
}
