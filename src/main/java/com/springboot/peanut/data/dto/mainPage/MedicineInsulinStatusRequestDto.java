package com.springboot.peanut.data.dto.mainPage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@AllArgsConstructor
public class MedicineInsulinStatusRequestDto {
    private boolean medicineStatus;
    private boolean insulinStatus;

}
