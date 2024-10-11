package com.springboot.peanut.data.repository.Medicine;

import com.springboot.peanut.data.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicineRepository extends JpaRepository<Medicine,Long>,MedicineRepositoryCustom {
    Optional<Medicine> findById(Long id);
    List<Medicine> findByUserId(Long id);

}
