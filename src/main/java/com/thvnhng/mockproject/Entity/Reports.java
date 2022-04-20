package com.thvnhng.mockproject.Entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Reports extends AbstractEntity{

    private Double studentMark;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Courses course;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subjects subject;

}
