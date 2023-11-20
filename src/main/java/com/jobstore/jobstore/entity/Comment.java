package com.jobstore.jobstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "comment")
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commentid;
    @Column(length = 255,nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "memberid",referencedColumnName = "memberid"),
    })
    private Member member;

    @ManyToOne
    @JoinColumn(name = "contentsid",referencedColumnName = "contentsid")
    private Contents contents;
}
