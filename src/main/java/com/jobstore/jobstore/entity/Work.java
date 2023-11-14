package com.jobstore.jobstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "work")
@Getter
@Setter
public class Work {
    @Id
    private long workid;
    @Column(length = 255,nullable = false)
    private String worktodo;
}
