package com.springboot.peanut.data.dao;

import com.springboot.peanut.data.entity.Medicine;

import java.util.List;

public interface MedicineDao {
    void saveMedicineInfo(Medicine medicine);
    List<Medicine> getMedicineByUserId(Long userId);

}
