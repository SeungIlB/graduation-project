package com.graduation.graduationproject.service;

import com.graduation.graduationproject.config.jwt.JwtTokenProvider;
import com.graduation.graduationproject.dto.JwtToken;
import com.graduation.graduationproject.dto.MemberDto;
import com.graduation.graduationproject.dto.SignUpDto;
import com.graduation.graduationproject.dto.UpdateDto;
import com.graduation.graduationproject.entity.Member;
import com.graduation.graduationproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    @Transactional
    @Override
    public JwtToken signIn(String username, String password) {
        // 1. username + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        return jwtToken;
    }

    @Transactional
    @Override
    public MemberDto signUp(SignUpDto signUpDto) {
        if (memberRepository.existsByUsername(signUpDto.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 ID 입니다.");
        }
        if (memberRepository.existsByNickname(signUpDto.getNickname())){
            throw new IllegalArgumentException("이미 사용중인 닉네임 입니다.");
        }
        // Password 암호화
        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword()); // ?? 이거 해줘야돼나?
        List<String> roles = new ArrayList<>();
        roles.add("USER");  // USER 권한 부여
        return MemberDto.toDto(memberRepository.save(signUpDto.toEntity(encodedPassword, roles)));
    }
    @Transactional
    @Override
    public MemberDto findById(Long id) {
        // 하나 조회할때 optional로 감싸줌
        Optional<Member> optionalMemberEntity = memberRepository.findById(id);
        if (optionalMemberEntity.isPresent()){
            return MemberDto.toDto(optionalMemberEntity.get()); // optional을 벗겨내서 entity -> dto 변환
        }else {
            return null;
        }
    }
    @Transactional
    @Override
    public MemberDto update(UpdateDto updateDto) {
        // 업데이트할 회원의 ID 가져오기
        Long memberId = updateDto.getId();

        // ID를 사용하여 회원 정보 조회
        Member memberToUpdate = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + memberId));

        // 업데이트할 필드 업데이트
        memberToUpdate.setUsername(updateDto.getUsername());
        memberToUpdate.setPassword(passwordEncoder.encode(updateDto.getPassword()));
        memberToUpdate.setName(updateDto.getName());
        memberToUpdate.setAge(updateDto.getAge());
        memberToUpdate.setNickname(updateDto.getNickname());
        memberToUpdate.setAddress(updateDto.getAddress());
        memberToUpdate.setPhone(updateDto.getPhone());

        // 엔티티 저장하여 업데이트 반영
        Member updatedMember = memberRepository.save(memberToUpdate);

        // 업데이트된 회원 정보를 MemberDto로 변환하여 반환
        return MemberDto.toDto(updatedMember);
    }
    @Transactional
    @Override
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("유저 정보가 존재하지 않습니다."));

        memberRepository.delete(member);
    }

}