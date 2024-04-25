package com.graduation.graduationproject.entity;

import com.graduation.graduationproject.dto.AuthDto;
import com.graduation.graduationproject.dto.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @javax.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String username; // Principal
    private String password; // Credential
    private String name;
    private String nickname;
    private Date birthdate;
    private String address;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role; // 사용자 권한
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Label> labels;

    // == 생성 메서드 == //
    public static User registerUser(AuthDto.SignupDto signupDto) {
        User user = new User();

        user.username = signupDto.getUsername();
        user.password = signupDto.getPassword();
        user.name = signupDto.getName();
        user.nickname = signupDto.getNickname();
        user.birthdate = signupDto.getBirthdate();
        user.address = signupDto.getAddress();
        user.phone = signupDto.getPhone();
        user.role = Role.USER;

        return user;
    }
}