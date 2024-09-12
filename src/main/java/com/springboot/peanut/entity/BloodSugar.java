package com.springboot.peanut.entity;

import com.springboot.peanut.dto.Insulin.InsulinRequestDto;
import com.springboot.peanut.dto.bloodSugar.BloodSugarRequestDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BloodSugar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bloodSugarLevel;

    private String measurementCondition;

    private String memo;

    private LocalDateTime measurementTime;

    private LocalDateTime create_At;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static BloodSugar createBloodSugar(BloodSugarRequestDto bloodSugarRequestDto, User user){
        BloodSugar bloodSugar = new BloodSugar();
        bloodSugar.bloodSugarLevel = bloodSugarRequestDto.getBloodSugarLevel();
        bloodSugar.measurementCondition = bloodSugarRequestDto.getMeasurementCondition();
        bloodSugar.memo = bloodSugarRequestDto.getMemo();
        bloodSugar.create_At = LocalDateTime.now();
        bloodSugar.user = user;
        return bloodSugar;
    }
}
