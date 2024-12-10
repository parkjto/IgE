package com.SW.IgE.DTO;

import lombok.Data;

import java.util.List;

@Data
public class UserUpdateDTO {
    private Integer id;
    private String name;
    private Integer age;
    private List<String> allergies;
    private String password;
}
