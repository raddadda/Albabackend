package com.jobstore.jobstore.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


import java.util.List;

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

    @JsonIgnore
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "storeid", referencedColumnName = "storeid")
    })
    private Store store;

    @JsonIgnore
    @OneToMany(mappedBy = "member",orphanRemoval = true)
    List<Payment> payments;
    @OneToMany(mappedBy = "member")
    List<Attendance> attendances;

    @OneToMany(mappedBy = "member")
    List<Comment> comments;

}
