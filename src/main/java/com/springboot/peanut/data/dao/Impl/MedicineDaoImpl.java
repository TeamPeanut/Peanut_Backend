package com.springboot.peanut.data.dao.Impl;

import com.springboot.peanut.data.dao.MedicineDao;
import com.springboot.peanut.data.entity.Medicine;
import com.springboot.peanut.data.repository.Medicine.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicineDaoImpl implements MedicineDao {

    private final MedicineRepository medicineRepository;

    @Override
    public void saveMedicineInfo(Medicine medicine) {
        medicineRepository.save(medicine);
    }

    @Override
    public List<Medicine> getMedicineByUserId(Long userId) {
        List<Medicine> medicine = medicineRepository.findByUserId(userId);
        return medicine;
    }
}
