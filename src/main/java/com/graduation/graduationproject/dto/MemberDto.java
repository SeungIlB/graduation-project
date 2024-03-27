package com.graduation.graduationproject.dto;

import com.graduation.graduationproject.entity.Member;
import lombok.*;

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
    private String age;
    private String nickname;
    private String address;
    private String phone;


    public static MemberDto toDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .password(member.getPassword())
                .name(member.getName())
                .age(member.getAge())
                .nickname(member.getNickname())
                .address(member.getAddress())
                .phone(member.getPhone()).build();
    }

    public Member toEntity() {
        return Member.builder()
                .id(id)
                .username(username)
                .name(name)
                .age(age)
                .nickname(nickname)
                .address(address)
                .phone(phone).build();
    }
}