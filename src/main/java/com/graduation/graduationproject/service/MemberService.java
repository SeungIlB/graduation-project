package com.graduation.graduationproject.service;

import com.graduation.graduationproject.dto.JwtToken;
import com.graduation.graduationproject.dto.MemberDto;
import com.graduation.graduationproject.dto.SignUpDto;

import org.springframework.transaction.annotation.Transactional;

public interface MemberService {
    @Transactional
    public JwtToken signIn(String username, String password);

    @Transactional
    MemberDto signUp(SignUpDto signUpDto);
}
