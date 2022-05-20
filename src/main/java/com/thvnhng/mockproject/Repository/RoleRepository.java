package com.thvnhng.mockproject.Repository;

import com.thvnhng.mockproject.Entity.ERoles;
import com.thvnhng.mockproject.Entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {

    Boolean existsByRoleName(ERoles eRoles);
    List<Roles> findByRoleNameIn(List<ERoles> strRoles);
    Roles findByRoleName(ERoles eRoles);
}
