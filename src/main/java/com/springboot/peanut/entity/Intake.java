package com.springboot.peanut.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Medicine medicine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


//    @ElementCollection
//    @MapKeyColumn(name = "medicineName")
//    private Map<String, String> medication = new HashMap<>();
// Intake 생성 메서드
    public static Intake createIntake(List<String> intakeDays, String intakeNumber, List<String> intakeTime, User user, Medicine medicine) {
        Intake intake = new Intake();
         intake.intakeDays = intakeDays;
         intake.intakeNumber = intakeNumber;
         intake.intakeTime = intakeTime;
         intake.create_At = LocalDate.now();
         intake.user = user;
         intake.medicine = medicine;
      return intake;
}

}
