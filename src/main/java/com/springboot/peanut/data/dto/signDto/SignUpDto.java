package com.springboot.peanut.data.dto.signDto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class SignUpDto {
private String name;
private String gender;
private String birth;
private String password;
private String weight;
private String height;
private String nickname;
private String phoneNumber;

}
