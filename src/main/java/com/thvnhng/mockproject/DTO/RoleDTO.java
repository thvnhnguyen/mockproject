package com.thvnhng.mockproject.DTO;

import com.thvnhng.mockproject.Entity.ERoles;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RoleDTO {

    private Long id;
    private String createBy;
    private String modifiedBy;
    private LocalDateTime createDate;
    private LocalDateTime lastModifiedDate;
    private ERoles roleName;

}
