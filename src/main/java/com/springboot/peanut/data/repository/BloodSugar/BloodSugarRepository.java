package com.springboot.peanut.data.repository.BloodSugar;

import com.springboot.peanut.data.entity.BloodSugar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BloodSugarRepository extends JpaRepository<BloodSugar,Long>,BloodSugarRepositoryCustom {
    BloodSugar findBloodSugarByUserId(Long userId);
    List<BloodSugar> findAllByUserId(Long userId);
}
