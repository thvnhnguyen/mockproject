package com.thvnhng.mockproject.payload.request;

import com.thvnhng.mockproject.Valid.Annotation.CheckUser;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotEmpty;

@Data
public class LoginRequest {
    @CheckUser
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 6, max = 25, message = "Username contains between 6 and 25 characters")
    private String username;
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 25, message = "Password contains between 6 and 25 characters")
    private String password;
}
