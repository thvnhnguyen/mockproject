package com.thvnhng.mockproject.payload.request;

import lombok.Data;

//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotEmpty;

@Data
public class LoginRequest {
//    @NotEmpty(message = "khong dc trong")
    private String username;
//    @NotEmpty(message = "khong dc trong")
    private String password;
}
