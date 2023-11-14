package com.jobstore.jobstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "attendance")
@Getter
@Setter
public class Attendance {
    @Id
    private long attendid;
    @Column(length = 255,nullable = false)
    private String start;
    @Column(length = 255,nullable = false)
    private String end;
    @Column(length = 255,nullable = false)
    private String registertime;
}
