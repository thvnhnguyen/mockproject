package com.thvnhng.mockproject.Repository;

import com.thvnhng.mockproject.Entity.Courses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Courses, Long> {

    Boolean existsCoursesById(Long id);
    Courses findByCourseName(String courseName);
    List<Courses> findAllByCourseNameContaining(String courseName);

}
