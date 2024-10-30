package com.springboot.peanut.data.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
public class MedicineRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate recordDate;

    private LocalTime recordTime;

    private boolean medicineStatus;

    private boolean guardianMedicineStatus;

    @ManyToOne
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static MedicineRecord createInsulinRecord(LocalDate date, boolean medicineStatus, User user, Medicine medicine) {
        MedicineRecord medicineRecord = new MedicineRecord();
        medicineRecord.recordDate = date;
        medicineRecord.medicineStatus = medicineStatus;
        medicineRecord.user = user;
        medicineRecord.medicine = medicine;
        medicineRecord.recordTime = LocalTime.now();
        return medicineRecord;
    }



}
