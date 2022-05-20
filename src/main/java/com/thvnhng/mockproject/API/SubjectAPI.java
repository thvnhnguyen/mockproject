package com.thvnhng.mockproject.API;

import com.thvnhng.mockproject.DTO.SubjectDTO;
import com.thvnhng.mockproject.Service.SubjectService;
import com.thvnhng.mockproject.payload.response.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@AllArgsConstructor
public class SubjectAPI {

    private final SubjectService subjectService;

    @GetMapping
    public List<SubjectDTO> list() {
        return subjectService.listAll();
    }

    @GetMapping("/{subject_id}")
    public ResponseEntity<?> detail(@PathVariable(name = "subject_id") Long subjectId) {
        SubjectDTO subjectDTO = subjectService.detail(subjectId);
        if (subjectDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("404 Not Found");
        }
        return ResponseEntity.ok().body(subjectDTO);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody SubjectDTO subjectDTO) {
        subjectService.create(subjectDTO);
        return ResponseEntity.ok().body(new MessageResponse("Created new subject with name : " + subjectDTO.getSubjectName()));
    }

    @PutMapping("/{subject_name}")
    public ResponseEntity<?> update(
            @PathVariable(name = "subject_name") String subjectName,
            @RequestBody SubjectDTO subjectDTO
    ) {
        subjectService.update(subjectDTO);
        return ResponseEntity.ok().body("Updated");
    }

    @PutMapping("")
    public ResponseEntity<?> active() {
        return null;
    }

    @DeleteMapping
    public ResponseEntity<?> inactive() {
        return null;
    }

}
