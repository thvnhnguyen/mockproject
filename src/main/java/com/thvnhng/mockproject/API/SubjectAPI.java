package com.thvnhng.mockproject.API;

import com.thvnhng.mockproject.DTO.SubjectDTO;
import com.thvnhng.mockproject.Service.SubjectService;
import com.thvnhng.mockproject.payload.response.MessageResponse;
import lombok.AllArgsConstructor;
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
        return null;
    }

    @GetMapping("/{id}")
    public SubjectDTO detail(@PathVariable(name = "id") Long id) {
        return null;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody SubjectDTO subjectDTO) {
        subjectService.create(subjectDTO);
        return ResponseEntity.ok().body(new MessageResponse("Created new subject with name : " + subjectDTO.getSubjectName()));
    }

    @PutMapping
    public ResponseEntity<?> update() {
        return null;
    }

    @DeleteMapping
    public ResponseEntity<?> delete() {
        return null;
    }

}
