package com.jobstore.jobstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "paymentadmin")
@Getter
@Setter
public class PaymentAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long paymentid;
    @Column
    private String memberid;
    @Column
    private long storeid;
    @Column
    private long sum;
    @Column
    private long month;
}
