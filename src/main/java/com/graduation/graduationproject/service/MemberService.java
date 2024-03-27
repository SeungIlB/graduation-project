package com.graduation.graduationproject.service;

import com.graduation.graduationproject.dto.JwtToken;
import com.graduation.graduationproject.dto.MemberDto;
import com.graduation.graduationproject.dto.SignUpDto;

import com.graduation.graduationproject.dto.UpdateDto;
import com.graduation.graduationproject.entity.Member;
import com.graduation.graduationproject.controller.ImageUploadController;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberService {
    @Transactional
    public JwtToken signIn(String username, String password);

    @Transactional
    MemberDto signUp(SignUpDto signUpDto);

    @Transactional
    MemberDto findById(Long id);

    @Transactional
    MemberDto update(UpdateDto updateDto);

    @Transactional
    void deleteMember(Long id);

}
