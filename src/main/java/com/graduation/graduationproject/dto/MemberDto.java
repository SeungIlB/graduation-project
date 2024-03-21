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
    private String name;
    private String address;
    private String phone;
    private String profileImg;

    public static MemberDto toDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .name(member.getName())
                .address(member.getAddress())
                .phone(member.getPhone()).build();
    }

    public Member toEntity() {
        return Member.builder()
                .id(id)
                .username(username)
                .name(name)
                .address(address)
                .phone(phone).build();
    }
}