package com.thvnhng.mockproject.Repository;

import com.thvnhng.mockproject.Entity.Courses;
import com.thvnhng.mockproject.Entity.Scores;
import com.thvnhng.mockproject.Entity.Subjects;
import com.thvnhng.mockproject.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Scores, Long> {

    List<Scores> findAllByDeletedAtIsNull();
    List<Scores> findAllByUserIn(List<Users> usersList);
    List<Scores> findAllByCourseIn(List<Courses> coursesList);
    List<Scores> findAllBySubjectIn(List<Subjects> subjectsList);

}
