package com.thvnhng.mockproject.API;

import com.thvnhng.mockproject.DTO.CourseDTO;
import com.thvnhng.mockproject.Service.CourseService;
import com.thvnhng.mockproject.constant.SystemConstant;
import com.thvnhng.mockproject.payload.response.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@AllArgsConstructor
public class CourseAPI {

    private final CourseService courseService;

    @GetMapping
    public List<CourseDTO> list() {
        return null;
    }

    @GetMapping("/{id}")
    public CourseDTO detail(@PathVariable(name = "id") Long id) {
        return null;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CourseDTO courseDTO) {
        courseService.create(courseDTO);
        return ResponseEntity.ok().body(new MessageResponse("Created new course"));
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody CourseDTO courseDTO) {
        courseService.update(courseDTO);
        return ResponseEntity.ok().body(new MessageResponse("Updated course"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> active(@PathVariable(name = "id") Long id) {
        if (courseService.checkExistId(id)) {
            courseService.setStatus(id, SystemConstant.ACTIVE_STATUS);
            return ResponseEntity.ok().body(new MessageResponse("User activated"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("No course be found with id = " + id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> inactive(@PathVariable(name = "id") Long id) {
        if (courseService.checkExistId(id)) {
            courseService.setStatus(id, SystemConstant.ACTIVE_STATUS);
            return ResponseEntity.ok().body(new MessageResponse("Course inactivated"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("No course be found with id = " + id));
    }

}
