package com.springboot.peanut.entity;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FoodPredict {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String foodName;

    private String imageUrl;

    private String accuracy;

    @Transient
    private MultipartFile image;
}
