package com.jobstore.jobstore.entity;

import jakarta.persistence.*;
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
    @Column(length = 255,nullable = true)
    private String gowork;
    @Column(length = 255,nullable = true)
    private String leavework;
    @Column(length = 255,nullable = false)
    private String registertime;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "memberid",referencedColumnName = "memberid"),
    })
    private Member member;
}
