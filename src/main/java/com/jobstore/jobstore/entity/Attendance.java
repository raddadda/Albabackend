package com.jobstore.jobstore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jobstore.jobstore.dto.AttendanceDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "attendance")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {
    @Id
    @GeneratedValue
    private long attendid;

    @Column(length = 255,nullable = false)
    private LocalDateTime start;
    @Column(length = 255,nullable = false)
    private LocalDateTime end;
    @Column(length = 255,nullable = true)
    private LocalDateTime gowork;
    @Column(length = 255,nullable = true)
    private LocalDateTime leavework;
    @Column(length = 255,nullable = true)
    private long wage;
    //@Column(length = 255,nullable = false)
    //private LocalDateTime registertime;

    @JsonIgnore
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "memberid",referencedColumnName = "memberid"),
    })
    private Member member;

}
