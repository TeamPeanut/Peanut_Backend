package com.springboot.peanut.data.dto.signDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class KakaoResponseDto {
    private String userName;
    private String phoneNumber;
    private String email;
    private String profileUrl;
    private String gender;
    private String birth;
}
