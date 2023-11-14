package com.jobstore.jobstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User {
    @Id
    @Column(length = 255,nullable = false)
    private String userid;
    @Column(length = 255,nullable = false)
    private String password;
    @Column(length = 255,nullable = false)
    private String phonenumber;
    @Column(length = 255,nullable = false)
    private String name;
    @Column(length = 255,nullable = false)
    private String role;
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="adminid",referencedColumnName = "adminid"),
            @JoinColumn(name="storeid",referencedColumnName = "storeid"),
    })
    private Admin admin;


}
