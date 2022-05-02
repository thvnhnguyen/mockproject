package com.thvnhng.mockproject.Repository;

import com.thvnhng.mockproject.Entity.Courses;
import com.thvnhng.mockproject.Entity.Reports;
import com.thvnhng.mockproject.Entity.Subjects;
import com.thvnhng.mockproject.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Reports, Long> {

    List<Reports> findAllByDeletedAtIsNull();
    List<Reports> findAllByUserIn(List<Users> usersList);
    List<Reports> findAllByCourseIn(List<Courses> coursesList);
    List<Reports> findAllBySubjectIn(List<Subjects> subjectsList);

}
