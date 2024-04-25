package com.graduation.graduationproject.service;

import com.google.rpc.context.AttributeContext;
import com.graduation.graduationproject.dto.AuthDto;
import com.graduation.graduationproject.entity.User;
import com.graduation.graduationproject.repository.UserDetailsImpl;
import com.graduation.graduationproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void registerUser(AuthDto.SignupDto signupDto) {
        User user = User.registerUser(signupDto);
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(Long userId, AuthDto.UpdateDto updateDto, String encodedPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // UpdateDto로부터 업데이트할 사용자 정보 가져오기
        String newUsername = updateDto.getUsername();
        String newPassword = encodedPassword;
        String newName = updateDto.getName();
        String newNickname = updateDto.getNickname();
        String newAddress = updateDto.getAddress();
        String newPhone = updateDto.getPhone();

        user.setUsername(newUsername);
        user.setPassword(newPassword);
        user.setName(newName);
        user.setNickname(newNickname);
        user.setAddress(newAddress);
        user.setPhone(newPhone);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    // 사용자 ID로 사용자를 조회하는 메서드
    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
    @Transactional
    public User getUserById(Long userId) {
        // UserRepository를 사용하여 userId에 해당하는 사용자 정보를 데이터베이스에서 조회합니다.
        return userRepository.findById(userId).orElse(null);
    }
    public Long getLoggedInUserId(UserDetailsImpl userDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && userDetails != null) {
            // 현재 로그인한 사용자의 UserDetails 객체와 매개변수로 전달된 UserDetails 객체를 비교하여 일치하는 경우 사용자 ID를 반환합니다.
            if (authentication.getPrincipal().equals(userDetails)) {
                // 여기서는 UserDetails에 사용자 ID가 포함되어 있다고 가정합니다.
                // 만약 UserDetails에 사용자 ID가 포함되어 있지 않다면 사용자의 정보를 저장하는 다른 방법을 사용해야 합니다.
                return userDetails.getId();
            }
        }

        return null; // 현재 로그인한 사용자가 없거나 인증되지 않았을 경우 또는 UserDetails가 null인 경우
    }
}
