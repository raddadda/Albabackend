package com.jobstore.jobstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "work")
@Getter
@Setter
public class Work {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long workid;
    @Column(nullable = false)
    private long storeid;
    @Column(length = 255,nullable = false)
    private String title;
    @Column(length = 255,nullable = false)
    private String date;

    @OneToOne(mappedBy = "work")
    private Contents contents;
}
