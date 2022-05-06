package com.thvnhng.mockproject.payload.request;

import com.thvnhng.mockproject.Exception.CheckUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor
public class SignUpRequest {

    @CheckUser
    @NotBlank(message = "Khong duoc trong")
    private String username;
    @NotBlank(message = "Khong duoc trong")
    private String password;
    @NotBlank(message = "Khong duoc trong")
    private String email;
    private List<String> roleList;
}
