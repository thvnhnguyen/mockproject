package com.thvnhng.mockproject.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "courses")
//@Data
@Getter
@Setter
public class Courses extends AbstractEntity{

    @Column(name = "course_name", nullable = false, unique = true)
    private String courseName;

    @Column(name = "school_year")
    private Integer schoolYear;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "course_user", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<Users> usersList;

    @OneToMany(mappedBy = "course")
    private List<Scores> scoresList;

}
