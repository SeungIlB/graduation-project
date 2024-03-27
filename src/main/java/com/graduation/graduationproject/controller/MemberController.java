package com.graduation.graduationproject.controller;

import com.graduation.graduationproject.dto.*;
import com.graduation.graduationproject.entity.Member;
import com.graduation.graduationproject.exception.ErrorCode;
import com.graduation.graduationproject.security.SecurityUtil;
import com.graduation.graduationproject.service.MemberService;
import io.swagger.models.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Request;
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
            @ApiResponse(responseCode = "400", description = "실패 - id에 해당하는 회원이 없음", content = @Content(schema = @Schema(implementation = ErrorCode.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음")
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
    @PutMapping("/update/{member_id}")
    public ResponseEntity<MemberDto> updateMember(@PathVariable Long member_id, @RequestBody UpdateDto updateDto) {
        updateDto.setId(member_id); // 업데이트할 사용자 ID 설정
        MemberDto updatedMemberDto = memberService.update(updateDto);
        return ResponseEntity.ok(updatedMemberDto);
    }

    @DeleteMapping("/delete/{member_id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long member_id) {
        memberService.deleteMember(member_id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/test")
    public String test() {
        return "success";
    }
}
