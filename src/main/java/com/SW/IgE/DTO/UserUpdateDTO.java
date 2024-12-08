package com.SW.IgE.DTO;

import lombok.Data;

import java.util.List;

@Data
public class UserUpdateDTO {
    private Integer id;
    private String name;
    private Integer age;
    private List<String> allergies;
    private String password; // 비밀번호 추가

    // 비밀번호 setter로 암호화 처리
}
