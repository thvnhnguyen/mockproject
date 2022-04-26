package com.thvnhng.mockproject.Service;

import com.thvnhng.mockproject.DTO.CourseDTO;

import java.util.List;

public interface CourseService {

    Boolean checkExistId(Long id);
    List<CourseDTO> listAll();
    CourseDTO detail(Long id);
    CourseDTO create(CourseDTO courseDTO);
    CourseDTO update(CourseDTO courseDTO);
    void setStatus(Long id, Integer status);
}
