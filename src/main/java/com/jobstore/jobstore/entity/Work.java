package com.jobstore.jobstore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    @JsonIgnore
    @OneToMany(mappedBy = "work", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "work", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Contents> contents = new ArrayList<>();

}
