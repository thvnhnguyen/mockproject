package com.thvnhng.mockproject.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Roles extends AbstractEntity{

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private ERoles roleName;
    public Roles() {

    }
    public Roles(ERoles roleName) {
        this.roleName = roleName;
    }

    @ManyToMany(mappedBy = "rolesList")
    private List<Users> usersList;

}
