package com.thvnhng.mockproject.API;

import com.thvnhng.mockproject.Service.AuthService;
import com.thvnhng.mockproject.Service.UserService;
import com.thvnhng.mockproject.payload.request.LoginRequest;
import com.thvnhng.mockproject.payload.request.SignUpRequest;
import com.thvnhng.mockproject.payload.response.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "authenticationAPI")
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthAPI {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        if (!userService.checkExistUsername(loginRequest.getUsername())) {
            return ResponseEntity.ok().body(new MessageResponse("Username does not exist"));
        }
        return ResponseEntity.ok(authService.signIn(loginRequest));
    }

    @PostMapping("/signup")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {

        if (signUpRequest.getUsername().length() < 7) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Minimum username length is 7"));
        }
        if (signUpRequest.getPassword().length() < 6 || signUpRequest.getPassword().length() > 25) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Password contains between 6 and 25 characters"));
        }
        if (signUpRequest.getEmail().length() < 10) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Invalid email"));
        }
        if (userService.checkExistUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        if (userService.checkExistEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }
        if (signUpRequest.getRoleList().size() == 0) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No roles are specified"));
        }
        authService.signUp(signUpRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

    }
}
