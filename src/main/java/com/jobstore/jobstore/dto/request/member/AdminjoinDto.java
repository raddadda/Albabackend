package com.jobstore.jobstore.dto.request.member;

import com.jobstore.jobstore.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminjoinDto {

    private String memberid;
    private String password;
    private String phonenumber;
    private String name;
    private String role;
    private String companyname;
    private String ceo;
    private String companynumber;


    public Member toEntity(String encodedPassword){
        return Member.builder()
                .memberid(this.memberid)
                .password(encodedPassword)
                .phonenumber(this.phonenumber)
                .name(this.name)
                .role("ADMIN").build();
    }
}
