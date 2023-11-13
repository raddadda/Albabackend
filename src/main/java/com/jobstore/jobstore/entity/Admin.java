package com.jobstore.jobstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "admin")
@Getter
@Setter
public class Admin {
    @Id
    @Column(length = 255,nullable = false)
    private String adminid;
    @Column(length = 255,nullable = false)
    private String password;
    @Column(length = 255,nullable = false)
    private String phonenumber;
    @Column(length = 255,nullable = false)
    private String name;
    @Column(length = 255,nullable = false)
    private String role;
    @Column(length = 255,nullable = false)
    private String companynumber;
    @Column(length = 255,nullable = false)
    private String ceo;
}
