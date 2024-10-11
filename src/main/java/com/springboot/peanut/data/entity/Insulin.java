package com.springboot.peanut.data.entity;

import com.springboot.peanut.data.dto.Insulin.InsulinRequestDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private boolean insulinStatus = false;

    @ElementCollection
    private List<LocalDateTime> actualAdministrationDates; // 실제 투약이 이루어진 날짜 목록

    private LocalDate create_At;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<InsulinRecord> insulinRecords = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Insulin createInsulin(InsulinRequestDto insulinRequestDto,  User user){
        Insulin insulin = new Insulin();
        insulin.productName = insulinRequestDto.getProductName();
        insulin.dosage = insulinRequestDto.getDosage();
        insulin.administrationTime = insulinRequestDto.getAdministrationTime();
        insulin.create_At = LocalDate.now();
        insulin.user = user;
        return insulin;
    }
    public void updateInsulinStatus(boolean status) {
        this.insulinStatus = status;
    }
}
