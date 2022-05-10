package com.thvnhng.mockproject.DTO;

import com.thvnhng.mockproject.Entity.ERoles;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
public class RoleDTO {

    private Long id;
    private String createBy;
    private String modifiedBy;
    private LocalDateTime createDate;
    private LocalDateTime lastModifiedDate;
    protected String deletedBy;
    protected LocalDateTime deletedAt;
    @NotEmpty(message = "Name of role cannot be empty")
    private ERoles roleName;

}
