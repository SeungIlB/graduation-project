package com.graduation.graduationproject.dto;


import com.graduation.graduationproject.entity.Member;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateDto {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String age;
    private String nickname;
    private String address;
    private String phone;



    public Member toEntity(String encodedPassword) {

        return Member.builder()
                .username(username)
                .password(encodedPassword)
                .name(name)
                .age(age)
                .nickname(nickname)
                .address(address)
                .phone(phone)
                .build();
    }
}
