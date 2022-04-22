package com.thvnhng.mockproject.Repository;

import com.thvnhng.mockproject.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsernameAndStatus(String username, Integer status);
    Boolean existsByUsernameAndEmail(String username, String email);
    Boolean existsUsersById(Long id);
    Boolean existsUsersByUsername(String username);
    Boolean existsUsersByEmail(String email);
    List<Users> findAllByStatus(Integer status);
    Users findByUsername(String username);
}
