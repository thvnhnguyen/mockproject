package com.thvnhng.mockproject.Service;

import com.thvnhng.mockproject.DTO.CourseDTO;
import com.thvnhng.mockproject.DTO.CourseResponseDTO;
import com.thvnhng.mockproject.DTO.UserDTO;
import com.thvnhng.mockproject.Entity.Courses;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

public interface CourseService {

    Boolean checkExistId(Long id);
    Boolean checkExistMainTeacher(Long courseId, Sort sort);
    Boolean checkExistSubjectTeacher(String courseName, Long userId);
    Boolean isExistCourse(String courseName);
    List<CourseResponseDTO> listAll(Sort sort);
    List<UserDTO> listStudent(Long id, Sort sort);
    List<UserDTO> listTeacher(Long id, Sort sort);
    CourseResponseDTO detail(Long id);
    CourseResponseDTO detailByCourseName(String courseName);
    CourseDTO create(CourseDTO courseDTO);
    CourseDTO update(CourseDTO courseDTO);
    void saveMainTeacher(Long courseId, UserDTO userDTO);
    void saveSubjectTeacher(String courseName, Long userId);
    boolean saveStudentToCourse(Long courseId, List<Long> userIds);
    void setDelete(Long id,String deletedBy, LocalDateTime deletedAt);
    CourseResponseDTO convertToDTO(Courses course);
    boolean checkTeacherPermission(String username, Long courseId);
}
