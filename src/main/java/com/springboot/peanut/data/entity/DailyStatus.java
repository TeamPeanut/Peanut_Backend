package com.springboot.peanut.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class DailyStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double avgBloodSugar;
    private String bloodSugarStatus;
    private String medicineStatus;
    private String insulinStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_note_id")
    private MedicalNote medicalNote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
