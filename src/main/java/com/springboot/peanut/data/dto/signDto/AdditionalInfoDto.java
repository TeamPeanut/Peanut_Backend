package com.springboot.peanut.data.dto.signDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class AdditionalInfoDto {

    private String nickName;

    private String height;

    private String weight;

}
