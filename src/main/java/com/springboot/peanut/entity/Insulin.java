package com.springboot.peanut.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Insulin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    private String dosage;

    @ElementCollection
    private List<String> administrationTime;

    private boolean alam = false;

    private LocalDate create_At;


}
