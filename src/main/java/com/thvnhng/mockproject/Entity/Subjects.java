package com.thvnhng.mockproject.Entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "subjects")
@Data
public class Subjects extends AbstractEntity{

    @Column(name = "subject_name")
    private String subjectName;

    @OneToMany(mappedBy = "subject")
    private List<Reports> reportsList;
}
