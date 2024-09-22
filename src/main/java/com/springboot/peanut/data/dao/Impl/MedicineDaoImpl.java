package com.springboot.peanut.data.dao.Impl;

import com.springboot.peanut.data.dao.MedicineDao;
import com.springboot.peanut.data.entity.Medicine;
import com.springboot.peanut.data.repository.Medicine.MedicineRepository;
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
