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
    @Column(length = 255,nullable = false)
    private String userid;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="adminid",referencedColumnName = "adminid"),
            @JoinColumn(name="storeid",referencedColumnName = "storeid"),
    })
    private Admin admin;
}
