package com.springboot.peanut.data.entity;

import com.springboot.peanut.data.dto.Insulin.InsulinRequestDto;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Insulin createInsulin(InsulinRequestDto insulinRequestDto,  User user){
        Insulin insulin = new Insulin();
        insulin.productName = insulinRequestDto.getProductName();
        insulin.dosage = insulinRequestDto.getDosage();
        insulin.administrationTime = insulinRequestDto.getAdministrationTime();
        insulin.alam = insulinRequestDto.isAlam();
        insulin.create_At = LocalDate.now();
        insulin.user = user;
        return insulin;
    }
}
