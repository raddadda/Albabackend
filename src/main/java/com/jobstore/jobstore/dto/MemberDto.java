package com.jobstore.jobstore.dto;


import com.jobstore.jobstore.entity.Member;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private String memberid;
    private String password;
    private String phonenumber;
    private String name;
    private String role;
    private String memberimg;


    public Member toEntity(String encodedPassword){
        return Member.builder()
                .memberid(this.memberid)
                .password(encodedPassword)
                .phonenumber(this.phonenumber)
                .name(this.name)
                .role("USER").build();

    }
}
