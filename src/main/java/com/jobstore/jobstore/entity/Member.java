package com.jobstore.jobstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "member")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @Column(length = 255, nullable = false)
    private String memberid;
    @Column(length = 255, nullable = false)
    private String password;
    @Column(length = 255, nullable = false)
    private String phonenumber;
    @Column(length = 255, nullable = false)
    private String name;
    @Column(length = 255, nullable = false)
    private String role;
    @Column(length = 255, nullable = true)
    private String memberimg;
}
