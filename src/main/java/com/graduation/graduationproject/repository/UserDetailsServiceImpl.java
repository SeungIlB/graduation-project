package com.graduation.graduationproject.repository;

import com.graduation.graduationproject.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Can't find user with this email. -> " + username));

        if(findUser != null){
            UserDetailsImpl userDetails = new UserDetailsImpl(findUser);
            return  userDetails;
        }

        return null;
    }
}
