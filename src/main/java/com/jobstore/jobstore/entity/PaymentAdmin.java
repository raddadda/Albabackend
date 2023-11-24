package com.jobstore.jobstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "paymentadmin")
@Getter
@Setter
public class PaymentAdmin {
    @Id
    private String memberid;
    @Column
    private long storeid;
    @Column
    private long sum;
    @Column
    private long month;
}
