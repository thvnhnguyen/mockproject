package com.thvnhng.mockproject.API;

import com.thvnhng.mockproject.Entity.ERoles;
import com.thvnhng.mockproject.Service.AuthService;
import com.thvnhng.mockproject.Service.SubjectService;
import com.thvnhng.mockproject.Service.UserService;
import com.thvnhng.mockproject.Valid.RegexString;
import com.thvnhng.mockproject.payload.request.LoginRequest;
import com.thvnhng.mockproject.payload.request.SignUpRequest;
import com.thvnhng.mockproject.payload.response.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController(value = "authenticationAPI")
@RequestMapping("")
@AllArgsConstructor
public class AuthAPI {

    private final AuthService authService;
    private final UserService userService;
    private final SubjectService subjectService;
    private final PasswordEncoder encoder;

    //Test ok
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        if (!username.matches(RegexString.USERNAME_PATTERN)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(
                            "Username is not valid," +
                                    " username contains only letters, numbers and '_' ," +
                                    " username must start with a letter and be between 7 and 30 characters long"));
        }
        if (!userService.checkExistUsername(username)) {
            return ResponseEntity.ok().body(new MessageResponse("Username does not exist"));
        }
        String encodedCurrentPassword = userService.getEncodedPassword(username);
        if (!encoder.matches(loginRequest.getPassword(), encodedCurrentPassword)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Password is not valid"));
        }
        return ResponseEntity.ok(authService.signIn(loginRequest));
    }

//    Test ok
    @PostMapping("/api/user/new")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (signUpRequest.getRoleUser().equals((ERoles.ROLE_SUBJECT_TEACHER.toString()))) {
            if (signUpRequest.getSubjectName() == null || "".equals(signUpRequest.getSubjectName())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Please choose a subject for " + ERoles.ROLE_SUBJECT_TEACHER));
            } else {
                if (!subjectService.isExistSubjectName(signUpRequest.getSubjectName())) {
                    return ResponseEntity.badRequest().body(new MessageResponse("Invalid subject for " + ERoles.ROLE_SUBJECT_TEACHER));
                }
            }
        }
        signUpRequest.setSubjectName(null);
        userService.signUp(signUpRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

    }
}
