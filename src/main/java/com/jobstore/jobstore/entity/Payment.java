package com.jobstore.jobstore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long payid;
    @Column(nullable = false)
    private long pay;
    @Column(nullable = true,length = 25)
    private long month;
    @Column(length = 255,nullable = true)
    private LocalDateTime register;

    @JsonIgnore
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "memberid",referencedColumnName = "memberid"),
            @JoinColumn(name = "storeid",referencedColumnName = "storeid")
    })
    private Member member;
}
