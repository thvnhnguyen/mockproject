package com.thvnhng.mockproject.API;

import com.thvnhng.mockproject.DTO.CourseDTO;
import com.thvnhng.mockproject.DTO.UserDTO;
import com.thvnhng.mockproject.Entity.ERoles;
import com.thvnhng.mockproject.Service.CourseService;
import com.thvnhng.mockproject.Service.ScoreService;
import com.thvnhng.mockproject.Service.UserService;
import com.thvnhng.mockproject.payload.response.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
@AllArgsConstructor
public class CourseAPI {

    private final CourseService courseService;
    private final UserService userService;
    private final ScoreService scoreService;

//    Api List course co filter, (paging and sort) ok
    @GetMapping
    public ResponseEntity<?> list(
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(value = "order", required = false, defaultValue = "desc") String order
    ) {
        try {
            if ("asc".equals(order)) {
                return ResponseEntity.ok(courseService.listAll(Sort.by(sortBy).ascending()));
            } else {
                return ResponseEntity.ok(courseService.listAll(Sort.by(sortBy).descending()));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("404 Not Found"));
        }
    }

//    Test ok
    @GetMapping("/{course_id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> detail(@PathVariable(name = "course_id") Long courseId) {
        if (!courseService.checkExistId(courseId)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Not found course with id = " + courseId));
        }
        if (!courseService.checkTeacherPermission(currentUsername(), courseId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("You cannot view detail of this class"));
        }
        return ResponseEntity.ok(courseService.detail(courseId));
    }

//    List student trong course ok
    @GetMapping("/{course_id}/students")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MAIN_TEACHER', 'ROLE_SUBJECT_TEACHER')")
    public ResponseEntity<?> listStudent(
            @PathVariable(name = "course_id") Long courseId,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(value = "order", required = false, defaultValue = "desc") String order
    ) {
        if (!courseService.checkExistId(courseId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found Course id = " + courseId);
        }
        if (!courseService.checkTeacherPermission(currentUsername(), courseId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("You cannot view detail of this class"));
        }
        try {
            if ("asc".equals(order)) {
                return ResponseEntity.ok(courseService.listStudent(courseId, Sort.by(sortBy).ascending()));
            } else {
                return ResponseEntity.ok(courseService.listStudent(courseId, Sort.by(sortBy).descending()));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("404 Not Found"));
        }
    }

    //List diem co filter theo username, subject
    @GetMapping("/{course_id}/transcript")
    public ResponseEntity<?> transcriptBy(
            @PathVariable(name = "course_id") Long id,
            @RequestParam(name = "username", required = false, defaultValue = "") String username,
            @RequestParam(name = "subject", required = false, defaultValue = "") String subject
    ) {
        String courseName = courseService.detail(id).getCourseName();
        return ResponseEntity.ok().body(scoreService.listBy(username, courseName, subject));
    }

//    List teacher trong course co sort ok
    @GetMapping("/{course_id}/teacherList")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MAIN_TEACHER')")
    public ResponseEntity<?> teacherList(
            @PathVariable(name = "course_id") Long id,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(value = "order", required = false, defaultValue = "desc") String order
    ) {
        if (!courseService.checkExistId(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found Course id = " + id);
        }
        if ("asc".equals(order)) {
            return ResponseEntity.ok(courseService.listTeacher(id, Sort.by(sortBy).ascending()));
        } else {
            return ResponseEntity.ok(courseService.listTeacher(id, Sort.by(sortBy).descending()));
        }
    }

//    Tao moi course
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> create(@Valid @RequestBody CourseDTO courseDTO) {
        courseService.create(courseDTO);
        return ResponseEntity.ok().body(new MessageResponse("Created new course"));
    }

//    Update course
    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MAIN_TEACHER')")
    public ResponseEntity<?> update(@RequestBody CourseDTO courseDTO) {
        courseService.update(courseDTO);
        return ResponseEntity.ok().body(new MessageResponse("Updated course"));
    }
// Not tested yet
    @PutMapping("/{course_id}/students/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addStudent(@PathVariable(name = "course_id") Long courseId, @RequestBody List<Long> userIds) {
        if (!courseService.checkExistId(courseId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Not Found Course id = " + courseId));
        }
        if (courseService.saveStudentToCourse(courseId, userIds)) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse("Students have been added to the class "));
        } else {
            return ResponseEntity.badRequest().body("List student id is not valid");
        }
    }

//    Add main teacher vao course
    @PutMapping("/{course_id}/teachers/addMain")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addMainTeacher(@PathVariable(name = "course_id") Long courseId, @RequestBody UserDTO userDTO) {
        if (!courseService.checkExistId(courseId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found Course id " + courseId);
        }
        if (courseService.checkExistMainTeacher(courseId, Sort.by("id"))) {
            return ResponseEntity.badRequest().body(new MessageResponse("The class has a main teacher"));
        }
        if (userService.detailByUsername(userDTO.getUsername()).getRoleList().contains(ERoles.ROLE_MAIN_TEACHER.toString())) {
            return ResponseEntity.badRequest().body(new MessageResponse("This teacher has been in charge of another class"));
        }
        courseService.saveMainTeacher(courseId, userDTO);
        return ResponseEntity.ok().body(new MessageResponse("Added "+userDTO.getFullName()+" to be main teacher of course"));
    }

//  Them teacher bo mon vao lop
    @PutMapping("/{course_id}/teachers/addSub")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addSubjectTeacher(@PathVariable(name = "course_id") String courseName, @PathVariable(name = "user_id") Long userId) {
        if (!courseService.isExistCourse(courseName)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found Course name = " + courseName);
        }
        if (courseService.checkExistSubjectTeacher(courseName, userId)) {
            return ResponseEntity.badRequest().body(new MessageResponse("The class has this subject teacher"));
        }
        courseService.saveSubjectTeacher(courseName, userId);
        return ResponseEntity.ok().body(new MessageResponse("Added user to be subject teacher of course"));
    }

//    Update GVBM
    @PutMapping("/{course_name}/update/{user_id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateSubjectTeacher(@PathVariable(name = "course_name") String courseName, @PathVariable(name = "user_id") Long userId) {
        courseService.saveSubjectTeacher(courseName, userId);
        return ResponseEntity.ok().body(new MessageResponse("Added user to be subject teacher of course"));
    }

//    Active course
    @PutMapping("/{course_id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> active(@PathVariable(name = "course_id") Long id) {
        if (courseService.checkExistId(id)) {
            courseService.setDelete(id, null, null);
            return ResponseEntity.ok().body(new MessageResponse("Course activated"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("No course be found with id = " + id));
    }

//    Inactive course
    @DeleteMapping("/{course_id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> inactive(@PathVariable(name = "course_id") Long id) {
        if (courseService.checkExistId(id)) {
            String deletedBy = SecurityContextHolder.getContext().getAuthentication().getName();
            courseService.setDelete(id, deletedBy, LocalDateTime.now());
            return ResponseEntity.ok().body(new MessageResponse("Course inactivated"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("No course be found with id = " + id));
    }

    private String currentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
