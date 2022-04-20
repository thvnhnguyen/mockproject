package com.thvnhng.mockproject.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {

    private Long id;
    private String username;
    private String password;
    private String email;
    private Character gender;
    private String contactNumber;
    private String address;
    private Date birthDate;
    private Integer status;
}
