package com.jobstore.jobstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "admin")
    private List<User> user=new ArrayList<User>();

    @OneToOne
    @JoinColumn(name = "storeid",referencedColumnName = "storeid")
    private Store store;

    @OneToMany(mappedBy = "admin")
    private List<Work> work=new ArrayList<Work>();

    @OneToMany(mappedBy = "admin")
    private List<Attendance> attendance=new ArrayList<Attendance>();

    @OneToMany(mappedBy = "admin")
    List<Payment> payment=new ArrayList<Payment>();
}
