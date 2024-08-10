package com.springboot.peanut.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrentBloodSugar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int current_blood_sugar;

    private LocalDate record_time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
