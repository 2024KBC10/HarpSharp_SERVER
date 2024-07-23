package com.harpsharp.auth.service;

import com.harpsharp.auth.dto.CustomUserDetails;
import com.harpsharp.auth.entity.UserEntity;
import com.harpsharp.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userData = userRepository
                .findByUsername(username)
                .orElseThrow(NullPointerException::new);

        if (userData != null) {
            return new CustomUserDetails(userData);
        }

        return null;
    }
}
