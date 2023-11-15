package com.jobstore.jobstore.entity;

<<<<<<< HEAD
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "member")
@Getter
@Setter
public class Member {
    @Id
    @Column(length = 255,nullable = false)
    private String memberid;
    @Column(length = 255,nullable = false)
    private String password;
    @Column(length = 255,nullable = false)
    private String phonenumber;
    @Column(length = 255,nullable = false)
    private String name;
    @Column(length = 255,nullable = false)
    private String role;
    @Column(length = 255,nullable = true)
    private String memberimg;
=======
public class Member {
>>>>>>> chan2
}
