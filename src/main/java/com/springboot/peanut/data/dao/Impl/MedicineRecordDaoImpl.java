package com.springboot.peanut.data.dao.Impl;

import com.springboot.peanut.data.dao.MedicineRecordDao;
import com.springboot.peanut.data.entity.MedicineRecord;
import com.springboot.peanut.data.repository.MedicineRecord.MedicineRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicineRecordDaoImpl implements MedicineRecordDao {

    private  final MedicineRecordRepository medicineRecordRepository;

    @Override
    public void saveMedicineRecord(MedicineRecord medicineRecord) {
        medicineRecordRepository.save(medicineRecord);
    }

    @Override
    public Optional<List<MedicineRecord>> findMedicineRecordByUserId(Long userId, LocalDate date) {
        return medicineRecordRepository.findMedicineRecordByUserId(userId, date);
    }
}
