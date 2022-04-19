package com.thvnhng.mockproject.Entity;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class Courses extends AbstractEntity{

    private String courseName;
}
