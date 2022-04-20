package com.thvnhng.mockproject.Repository;

import com.thvnhng.mockproject.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsernameAndStatus(String username, Integer status);
    Boolean existsUsersByUsernameAndPassword(String username, String password);
    Boolean existsUsersByUsername(String username);
    Boolean existsUsersByEmail(String email);

}
