package com.springboot.peanut.dto.signDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AdditionalInfoDto {

    private String nickName;

    private String height;

    private String weight;
}
