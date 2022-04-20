package com.thvnhng.mockproject.Service.impl;

import com.thvnhng.mockproject.Repository.UserRepository;
import com.thvnhng.mockproject.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Boolean checkExistUsername(String username) {
        return userRepository.existsUsersByUsername(username);
    }

    @Override
    public Boolean checkExistUsernamePassword(String username, String password) {
        return userRepository.existsUsersByUsernameAndPassword(username, password);
    }

    @Override
    public Boolean checkExistEmail(String email) {
        return userRepository.existsUsersByEmail(email);
    }
}
