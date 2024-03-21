package com.graduation.graduationproject.dto;

import com.graduation.graduationproject.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpDto {

    private String username;
    private String password;
    private String name;
    private String address;
    private String phone;
    private String profileImg;
    private List<String> roles = new ArrayList<>();

    public Member toEntity(String encodedPassword, List<String> roles) {

        return Member.builder()
                .username(username)
                .password(encodedPassword)
                .name(name)
                .address(address)
                .phone(phone)
                .roles(roles)
                .build();
    }
}