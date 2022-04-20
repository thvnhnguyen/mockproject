package com.thvnhng.mockproject.Service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thvnhng.mockproject.DTO.UserDTO;
import com.thvnhng.mockproject.Entity.ERoles;
import com.thvnhng.mockproject.Entity.Roles;
import com.thvnhng.mockproject.Entity.Users;
import com.thvnhng.mockproject.Repository.RoleRepository;
import com.thvnhng.mockproject.Repository.UserRepository;
import com.thvnhng.mockproject.Security.jwt.JwtUtils;
import com.thvnhng.mockproject.Security.services.UserDetailsImpl;
import com.thvnhng.mockproject.Service.AuthService;
import com.thvnhng.mockproject.constant.SystemConstant;
import com.thvnhng.mockproject.payload.request.LoginRequest;
import com.thvnhng.mockproject.payload.request.SignUpRequest;
import com.thvnhng.mockproject.payload.response.JwtResponse;
import com.thvnhng.mockproject.payload.response.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder encoder;
    private final ObjectMapper objectMapper;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public JwtResponse signIn(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return new JwtResponse(jwt, userDetails.getUsername(), userDetails.getEmail(), roles);
    }

    @Override
    public UserDTO signUp(SignUpRequest signUpRequest) {
        Users user = objectMapper.convertValue(signUpRequest, Users.class);
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setStatus(SystemConstant.ACTIVE_STATUS);
        List<String> strRoles = signUpRequest.getRoleList();
        List<ERoles> eRolesList = new ArrayList<>();
        for (String strRole : strRoles) {
            if (strRole.equals(ERoles.ROLE_ADMIN.name())) {
                eRolesList.add(ERoles.ROLE_ADMIN);
            } else if (strRole.equals(ERoles.ROLE_STUDENT.name())) {
                eRolesList.add(ERoles.ROLE_STUDENT);
            }
        }
        List<Roles> roles = roleRepository.findByRoleNameIn(eRolesList);
        user.setRolesList(roles);
        return objectMapper.convertValue(userRepository.save(user), UserDTO.class);
    }
}