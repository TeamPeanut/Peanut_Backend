package com.springboot.peanut.data.entity;

import com.springboot.peanut.data.dto.Insulin.InsulinRequestDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Entity
@Getter
@Setter
public class InsulinRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate recordDate;

    private LocalTime recordTime;

    private boolean insulinStatus;

    private  boolean guardianStatus;

    @ManyToOne
    @JoinColumn(name = "insulin_id")
    private Insulin insulin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static InsulinRecord createInsulinRecord(LocalDate date,boolean insulinStatus, User user, Insulin insulin) {
        InsulinRecord insulinRecord = new InsulinRecord();
        insulinRecord.recordDate = date;
        insulinRecord.insulinStatus = insulinStatus;
        insulinRecord.user = user;
        insulinRecord.insulin = insulin;
        insulinRecord.recordTime = LocalTime.now();
        return insulinRecord;
    }


}
