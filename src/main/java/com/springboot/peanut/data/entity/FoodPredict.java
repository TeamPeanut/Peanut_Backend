package com.springboot.peanut.data.entity;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDate;

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

    private LocalDate create_At;

    @Transient
    private MultipartFile image;

    public static FoodPredict createFoodPredict(String foodName ,String imageUrl, String accuracy) {
        FoodPredict foodPredict = new FoodPredict();
        foodPredict.setFoodName(foodName);
        foodPredict.setImageUrl(imageUrl);
        foodPredict.setAccuracy(accuracy);
        foodPredict.create_At = LocalDate.now();
        return foodPredict;
    }

}
