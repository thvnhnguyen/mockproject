package com.thvnhng.mockproject.API;

import com.thvnhng.mockproject.DTO.CourseDTO;
import com.thvnhng.mockproject.DTO.ReportDTO;
import com.thvnhng.mockproject.DTO.UserDTO;
import com.thvnhng.mockproject.Service.CourseService;
import com.thvnhng.mockproject.payload.response.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@AllArgsConstructor
public class CourseAPI {

    private final CourseService courseService;

//    Api List course co filter, (paging and sort)
    @GetMapping
    public List<CourseDTO> list(
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        return null;
    }

//    Detail course theo id
    @GetMapping("/{course_id}")
    public ResponseEntity<?> detail(@PathVariable(name = "course_id") Long id) {
        if (!courseService.checkExistId(id)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Not found course with id = " + id));
        }
        return ResponseEntity.ok(courseService.detail(id));
    }

//    List student trong course
    @GetMapping("/{course_id}/listStudent")
    public ResponseEntity<?> listStudent(
            @PathVariable(name = "course_id") Long id,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        if (size > 50) {
            size = 50;
        }
        if ("asc".equals(order)) {
            Pageable pageWithLimitAndSort = PageRequest.of(page, size, Sort.by(sortBy).ascending());
            return ResponseEntity.ok(courseService.listStudent(id, pageWithLimitAndSort));
        } else {
            if ("desc".equals(order)) {
                Pageable pageWithLimitAndSort = PageRequest.of(page, size, Sort.by(sortBy).descending());
                return ResponseEntity.ok(courseService.listStudent(id, pageWithLimitAndSort));
            }
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong"));
    }

    //List diem co filter theo username, subject
    @GetMapping("/{course_id}/transcript")
    public List<ReportDTO> transcriptBy(
            @PathVariable(name = "course_id") Long id,
            @RequestParam(name = "username", required = false, defaultValue = "") String username,
            @RequestParam(name = "subject", required = false, defaultValue = "") String subject
    ) {
//        return reportService.listBy(username, course, subject);
        return null;
    }

//    List teacher trong course co filter
    @GetMapping("/{course_id}/teacherList")
    public List<ReportDTO> teacherList(
            @PathVariable(name = "course_id") Long id,
            @RequestParam(name = "username", required = false, defaultValue = "") String username,
            @RequestParam(name = "subject", required = false, defaultValue = "") String subject
    ) {
        return null;
    }

//    Tao moi course
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CourseDTO courseDTO) {
        courseService.create(courseDTO);
        return ResponseEntity.ok().body(new MessageResponse("Created new course"));
    }

//    Update course
    @PutMapping
    public ResponseEntity<?> update(@RequestBody CourseDTO courseDTO) {
        courseService.update(courseDTO);
        return ResponseEntity.ok().body(new MessageResponse("Updated course"));
    }

//    Add main teacher vao course
    @PutMapping("/{id}/addMainTeacher")
    public ResponseEntity<?> addMainTeacher(@PathVariable(name = "id") Long id, @RequestBody UserDTO userDTO) {
        courseService.saveMainTeacher(id, userDTO);
        return ResponseEntity.ok().body(new MessageResponse("Added "+userDTO.getUsername()+" to be main teacher of course"));
    }

//  Them teacher bo mon vao lop
    @PutMapping("/{course_id}/addSubjectTeacher/{user_id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addSubjectTeacher(@PathVariable(name = "course_id") Long courseId, @PathVariable(name = "user_id") Long userId) {
        courseService.saveSubjectTeacher(courseId, userId);
        return ResponseEntity.ok().body(new MessageResponse("Added user to be subject teacher of course"));
    }

//    Active course
    @PutMapping("/{id}")
    public ResponseEntity<?> active(@PathVariable(name = "id") Long id) {
        if (courseService.checkExistId(id)) {
            courseService.setDelete(id, null, null);
            return ResponseEntity.ok().body(new MessageResponse("User activated"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("No course be found with id = " + id));
    }

//    Inactive course
    @DeleteMapping("/{id}")
    public ResponseEntity<?> inactive(@PathVariable(name = "id") Long id) {
        if (courseService.checkExistId(id)) {
            courseService.setDelete(id, "No Name", null);
            return ResponseEntity.ok().body(new MessageResponse("Course inactivated"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("No course be found with id = " + id));
    }

}
