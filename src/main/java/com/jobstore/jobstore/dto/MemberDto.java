package com.jobstore.jobstore.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private String memberid;
    private String password;
    private String phonenumber;
    private String name;
    private String role;
    private String memberimg;
}
