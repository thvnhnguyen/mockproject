package com.thvnhng.mockproject.Service;

import com.thvnhng.mockproject.DTO.CourseDTO;
import com.thvnhng.mockproject.DTO.UserDTO;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface CourseService {

    Boolean checkExistId(Long id);
    List<CourseDTO> listAll();
    List<UserDTO> listStudent(Long id, Pageable pageable);
    CourseDTO detail(Long id);
    CourseDTO create(CourseDTO courseDTO);
    CourseDTO update(CourseDTO courseDTO);
    void saveMainTeacher(Long id, UserDTO userDTO);
    void saveSubjectTeacher(Long courseId, Long userId);
    void setDelete(Long id,String deletedBy, LocalDateTime deletedAt);
    void convertToDTO(List<String> stringList, List<?> entityList);
}
