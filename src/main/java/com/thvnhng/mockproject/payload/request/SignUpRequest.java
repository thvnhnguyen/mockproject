package com.thvnhng.mockproject.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SignUpRequest {

    private String username;
    private String password;
    private String email;
    private List<String> roleList;
}
