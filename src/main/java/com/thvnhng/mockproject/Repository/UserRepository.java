package com.thvnhng.mockproject.Repository;

import com.thvnhng.mockproject.Entity.Courses;
import com.thvnhng.mockproject.Entity.Roles;
import com.thvnhng.mockproject.Entity.Subjects;
import com.thvnhng.mockproject.Entity.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsernameAndDeletedAtIsNull(String username);
    Boolean existsByUsernameAndEmail(String username, String email);
    Boolean existsUsersById(Long id);
    Boolean existsUsersByUsername(String username);
    Boolean existsUsersByEmail(String email);
    List<Users> findAllByDeletedAtIsNotNull();
    List<Users> findAllByDeletedAtIsNull(Pageable pageable);
    List<Users> findAllByUsernameContaining(String username);
    List<Users> findAllByCoursesListContainingAndRolesListContaining(Courses courses, Roles roles, Sort sort);
    List<Users> findAllBySubjectUser(Subjects subject);
    Users findAllByUsernameAndDeletedAtIsNull(String username);
    Users findUsersByIdAndDeletedAtIsNull(Long id);
    Users findByUsername(String username);
    List<Users> findAllByUsernameIn(List<String> userList);
}
