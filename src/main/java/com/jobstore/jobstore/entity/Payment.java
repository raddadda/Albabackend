package com.jobstore.jobstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "payment")
@Getter
@Setter
public class Payment {
    @Id
    private long payid;
    @Column(nullable = false)
    private long pay;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "memberid",referencedColumnName = "memberid"),
            @JoinColumn(name = "storeid",referencedColumnName = "storeid")
    })
    private Member member;
}
