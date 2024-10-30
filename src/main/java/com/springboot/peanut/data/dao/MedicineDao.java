package com.springboot.peanut.data.dao;

import com.springboot.peanut.data.entity.Medicine;

import java.util.List;
import java.util.Optional;

public interface MedicineDao {
    void saveMedicineInfo(Medicine medicine);
    List<Medicine> getMedicineByUserId(Long userId);
    Optional<Medicine> findMedicineByUserId(Long userId);
    }
