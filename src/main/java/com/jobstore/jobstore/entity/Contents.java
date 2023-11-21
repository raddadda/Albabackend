package com.jobstore.jobstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "contents")
@Getter
@Setter
public class Contents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long contentsid;
    @Column(length = 255,nullable = false)
    private String contents;
    @Column(length = 25,nullable = true)
    private String checked;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "workid",referencedColumnName = "workid"),
    })
    private Work work;

}
