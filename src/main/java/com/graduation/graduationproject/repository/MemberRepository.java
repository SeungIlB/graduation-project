package com.graduation.graduationproject.repository;

import com.graduation.graduationproject.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);
}
