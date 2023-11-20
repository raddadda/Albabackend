package com.jobstore.jobstore.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "contents")
public class Contents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long contentsid;
    @Column(length = 255,nullable = false)
    private String contents;
    @Column(length = 25,nullable = true)
    private String checked;

    @OneToMany(mappedBy = "contents")
    private List<Comment> comments;

    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "workid",referencedColumnName = "workid"),
    })
    private Work work;

}
