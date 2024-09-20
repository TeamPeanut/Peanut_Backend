package com.springboot.peanut.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String medicineName;

    private boolean alam = false;

    private LocalDate create_At;

    @OneToMany(mappedBy = "medicine",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<Intake> intakes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public static Medicine createMedicine(String medicineName , User user){
        Medicine medicine = new Medicine();
        medicine.medicineName=medicineName;
        medicine.alam = false;
        medicine.create_At = LocalDate.now();
        medicine.user = user;
        return medicine;
    }

    public void setAlam(boolean alam){
        this.alam=alam;
    }

    public void addIntake(Intake intake){
        intakes.add(intake);
        intake.setMedicine(this);
    }

}
