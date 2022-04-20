package com.thvnhng.mockproject.Security.services;

import com.thvnhng.mockproject.Entity.Users;
import com.thvnhng.mockproject.Repository.UserRepository;
import com.thvnhng.mockproject.constant.SystemConstant;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    public final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsernameAndStatus(username, SystemConstant.ACTIVE_STATUS)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return UserDetailsImpl.build(user);
    }
}
