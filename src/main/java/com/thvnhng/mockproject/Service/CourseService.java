package com.thvnhng.mockproject.Service;

import com.thvnhng.mockproject.DTO.CourseDTO;
import com.thvnhng.mockproject.DTO.UserDTO;
import com.thvnhng.mockproject.Entity.Courses;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

public interface CourseService {

    Boolean checkExistId(Long id);
    Boolean checkExistMainTeacher(String courseName, Sort sort);
    Boolean checkExistSubjectTeacher(String courseName, Long userId);
    Boolean isExistCourse(String courseName);
    List<CourseDTO> listAll(Sort sort);
    List<UserDTO> listStudent(Long id, Sort sort);
    List<UserDTO> listTeacher(Long id, Sort sort);
    CourseDTO detail(Long id);
    CourseDTO detailByCourseName(String courseName);
    CourseDTO create(CourseDTO courseDTO);
    CourseDTO update(CourseDTO courseDTO);
    void saveMainTeacher(String courseName, UserDTO userDTO);
    void saveSubjectTeacher(String courseName, Long userId);
    void setDelete(Long id,String deletedBy, LocalDateTime deletedAt);
    CourseDTO convertToDTO(Courses course);
}
