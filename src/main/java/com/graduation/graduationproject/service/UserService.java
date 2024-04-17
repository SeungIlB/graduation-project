package com.graduation.graduationproject.service;

import com.google.rpc.context.AttributeContext;
import com.graduation.graduationproject.dto.AuthDto;
import com.graduation.graduationproject.entity.User;
import com.graduation.graduationproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
        String newlabel = updateDto.getLabel();
        // 기타 업데이트할 필드들을 가져오세요.

        // 사용자 정보 업데이트
        user.setUsername(newUsername);
        user.setPassword(newPassword);
        user.setName(newName);
        user.setNickname(newNickname);
        user.setAddress(newAddress);
        user.setPhone(newPhone);
        user.setLabel(newlabel);

        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
