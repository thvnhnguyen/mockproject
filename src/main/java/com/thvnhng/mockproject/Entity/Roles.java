package com.thvnhng.mockproject.Entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Data
public class Roles extends AbstractEntity{

    @Column(name = "role_name")
    private String roleName;

}
