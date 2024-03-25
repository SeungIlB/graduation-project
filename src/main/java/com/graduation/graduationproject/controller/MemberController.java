package com.graduation.graduationproject.controller;

import com.graduation.graduationproject.dto.JwtToken;
import com.graduation.graduationproject.dto.MemberDto;
import com.graduation.graduationproject.dto.SignInDto;
import com.graduation.graduationproject.dto.SignUpDto;
import com.graduation.graduationproject.exception.ErrorCode;
import com.graduation.graduationproject.security.SecurityUtil;
import com.graduation.graduationproject.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;


    @PostMapping("/log-in")
    @Operation(summary = "로그인", description = "로그인 후 토큰 발급.", responses = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = MemberDto.class))),
            @ApiResponse(responseCode = "400", description = "실패 - id에 해당하는 회원이 없음", content = @Content(schema = @Schema(implementation = ErrorCode.class)))
    })
    public JwtToken signIn(@RequestBody SignInDto signInDto) {
        String username = signInDto.getUsername();
        String password = signInDto.getPassword();
        JwtToken jwtToken = memberService.signIn(username, password);
        log.info("request username = {}, password = {}", username, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
        return jwtToken;
    }



    @PostMapping("/sign-up")
    public ResponseEntity<MemberDto> signUp(@RequestBody SignUpDto signUpDto) {
        MemberDto savedMemberDto = memberService.signUp(signUpDto);
        return ResponseEntity.ok(savedMemberDto);
    }

    @PostMapping("/test")
    public String test() {
        return "success";
    }
}
