package com.thvnhng.mockproject.Entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public abstract class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "create_by")
    protected String createBy;

    @Column(name = "modified_by")
    protected String modifiedBy;

    @Column(name = "create_date")
    protected LocalDateTime createDate;

    @Column(name = "last_modified_date")
    protected LocalDateTime lastModifiedDate;


}
