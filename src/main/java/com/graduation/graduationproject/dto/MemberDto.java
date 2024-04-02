package com.graduation.graduationproject.dto;

import com.graduation.graduationproject.entity.Member;
import lombok.*;

import java.util.Date;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDto {

    private Long id;
    private String username;
    private String password;
    private String name;
    private Date age;
    private String nickname;
    private String address;
    private String phone;
    private String label;


    public static MemberDto toDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .password(member.getPassword())
                .name(member.getName())
                .age(member.getAge())
                .nickname(member.getNickname())
                .address(member.getAddress())
                .phone(member.getPhone())
                .label(member.getLabel())
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .id(id)
                .username(username)
                .name(name)
                .age(age)
                .nickname(nickname)
                .address(address)
                .phone(phone)
                .label(label)
                .build();
    }
}