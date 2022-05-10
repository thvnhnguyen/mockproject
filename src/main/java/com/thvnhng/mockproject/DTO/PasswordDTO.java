package com.thvnhng.mockproject.DTO;

import lombok.Data;

@Data
public class PasswordDTO {

    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;

}
