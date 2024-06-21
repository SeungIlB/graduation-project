package com.graduation.graduationproject.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String nickname;
    private Date birthdate;
    private String address;
    private String phone;
}