package com.thvnhng.mockproject.Service;

import com.thvnhng.mockproject.DTO.UserDTO;
import com.thvnhng.mockproject.payload.request.LoginRequest;
import com.thvnhng.mockproject.payload.request.SignUpRequest;
import com.thvnhng.mockproject.payload.response.JwtResponse;

public interface AuthService {

    JwtResponse signIn(LoginRequest loginRequest);
    UserDTO signUp(SignUpRequest signUpRequest);

}
