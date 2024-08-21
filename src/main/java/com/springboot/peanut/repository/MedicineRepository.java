package com.springboot.peanut.repository;

import com.springboot.peanut.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicineRepository extends JpaRepository<Medicine,Long> {
    Optional<Medicine> findById(Long id);

}
