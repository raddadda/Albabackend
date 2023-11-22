package com.jobstore.jobstore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    @Column(nullable = false,length = 25)
    private long month;

    @JsonIgnore
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "memberid",referencedColumnName = "memberid"),
            @JoinColumn(name = "storeid",referencedColumnName = "storeid")
    })
    private Member member;
}
