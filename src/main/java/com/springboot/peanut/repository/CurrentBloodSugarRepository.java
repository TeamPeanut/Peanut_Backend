package com.springboot.peanut.repository;

import com.springboot.peanut.entity.CurrentBloodSugar;
import com.springboot.peanut.entity.FastingBloodSugar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrentBloodSugarRepository extends JpaRepository<CurrentBloodSugar,Long> {


}
