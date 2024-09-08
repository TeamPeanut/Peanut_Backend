package com.springboot.peanut.dao.Impl;

import com.springboot.peanut.dao.MedicineDao;
import com.springboot.peanut.entity.Medicine;
import com.springboot.peanut.repository.Medicine.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicineDaoImpl implements MedicineDao {

    private final MedicineRepository medicineRepository;

    @Override
    public void saveMedicineInfo(Medicine medicine) {
        medicineRepository.save(medicine);
    }
}
