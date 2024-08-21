package com.springboot.peanut.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Intake {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<String> intakeDays;

    private String intakeNumber;

    @ElementCollection
    private List<String> intakeTime;

    private LocalDate create_At;

    @ManyToOne
    @JoinColumn(name="medicine_id")
    private Medicine medicine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


//    @ElementCollection
//    @MapKeyColumn(name = "medicineName")
//    private Map<String, String> medication = new HashMap<>();


}
