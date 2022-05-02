package com.thvnhng.mockproject.API;

import com.thvnhng.mockproject.DTO.ReportDTO;
import com.thvnhng.mockproject.DTO.ReportDTO;
import com.thvnhng.mockproject.Service.ReportService;
import com.thvnhng.mockproject.payload.response.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@AllArgsConstructor
public class ReportAPI {

    private final ReportService reportService;
    @GetMapping("/all")
    public List<ReportDTO> list() {
        return null;
    }

    @GetMapping
    public List<ReportDTO> listBy(
            @RequestParam(name = "username", required = false, defaultValue = "") String username,
            @RequestParam(name = "course", required = false, defaultValue = "") String course,
            @RequestParam(name = "subject", required = false, defaultValue = "") String subject
    ) {
        return reportService.listBy(username, course, subject);
    }

    @GetMapping("/{id}")
    public ReportDTO detail(@PathVariable(name = "id") Long id) {
        return null;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ReportDTO reportDTO) {
        reportService.create(reportDTO);
        return ResponseEntity.ok().body(new MessageResponse(
                "Created new report with username : " + reportDTO.getUsername() +
                ", course : " + reportDTO.getCourseName() +
                ", subject : " + reportDTO.getSubjectName()));
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
