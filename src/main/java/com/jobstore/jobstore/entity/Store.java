package com.jobstore.jobstore.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "store")
@Getter
@Setter
public class Store {
    @Id
    private long storeid;
    @Column(length = 255,nullable = false)
    private String companyname;
    @Column(length = 255,nullable = false)
    private String ceo;
    @Column(length = 255,nullable = false)
    private String companynumber;
    @Column(length = 255,nullable = true)
    private String companyimg;

    @OneToMany(mappedBy = "store")
    List<Member> members;
}
