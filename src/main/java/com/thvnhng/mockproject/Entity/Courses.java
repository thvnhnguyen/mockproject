package com.thvnhng.mockproject.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "courses")
@Data
public class Courses extends AbstractEntity{

    @Column(name = "course_name", nullable = false, unique = true)
    private String courseName;

    @Column(name = "school_year")
    private Integer schoolYear;

    @ManyToMany(mappedBy = "coursesList")
    private List<Users> usersList;

    @OneToMany(mappedBy = "course")
    private List<Reports> reportsList;

}
