package com.harpsharp.auth.service;

import com.harpsharp.auth.utils.CustomUserDetails;
import com.harpsharp.infra_rds.entity.user.User;
import com.harpsharp.infra_rds.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userData = userRepository
                .findByUsername(username)
                .orElseThrow(NullPointerException::new);

        if (userData != null) {
            return new CustomUserDetails(userData);
        }

        return null;
    }
}
