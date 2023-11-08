package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class MemberDto {

    @NotBlank(message = "닉네임에 공백은 불가능 합니다.")
    private String nick;

    @NotBlank(message = "비밀번호에 공백은 불가능 합니다.")
    private String password;

}
