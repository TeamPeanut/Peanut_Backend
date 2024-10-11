package com.springboot.peanut.data.repository.MedicineRecord;

import com.springboot.peanut.data.entity.InsulinRecord;
import com.springboot.peanut.data.entity.MedicineRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicineRecordRepository extends JpaRepository<MedicineRecord, Long>,MedicineRecordRepositoryCustom {

}
