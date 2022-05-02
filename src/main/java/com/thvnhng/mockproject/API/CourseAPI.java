package com.thvnhng.mockproject.API;

import com.thvnhng.mockproject.DTO.CourseDTO;
import com.thvnhng.mockproject.DTO.UserDTO;
import com.thvnhng.mockproject.Service.CourseService;
import com.thvnhng.mockproject.payload.response.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@AllArgsConstructor
public class CourseAPI {

    private final CourseService courseService;

    @GetMapping
    public List<CourseDTO> list(
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        return null;
    }

    @GetMapping("/{course_id}")
    public CourseDTO detail(@PathVariable(name = "course_id") Long id) {
        return courseService.detail(id);
    }

    @GetMapping("/{course_id}/listStudent")
    public List<UserDTO> listStudent(
            @PathVariable(name = "course_id") Long id,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        if (size > 100) {
            size = 100;
        }
        if ("asc".equals(order)) {
            Pageable pageWithLimitAndSort = PageRequest.of(page, size, Sort.by(sortBy).ascending());
            return courseService.listStudent(id, pageWithLimitAndSort);
        } else {
            if ("desc".equals(order)) {
                Pageable pageWithLimitAndSort = PageRequest.of(page, size, Sort.by(sortBy).descending());
                return courseService.listStudent(id, pageWithLimitAndSort);
            }
        }
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
            courseService.setDelete(id, null, null);
            return ResponseEntity.ok().body(new MessageResponse("User activated"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("No course be found with id = " + id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> inactive(@PathVariable(name = "id") Long id) {
        if (courseService.checkExistId(id)) {
            courseService.setDelete(id, "No Name", null);
            return ResponseEntity.ok().body(new MessageResponse("Course inactivated"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("No course be found with id = " + id));
    }

}
