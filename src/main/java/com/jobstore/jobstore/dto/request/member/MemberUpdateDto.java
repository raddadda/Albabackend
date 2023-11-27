package com.jobstore.jobstore.dto.request.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateDto {
    private String memberid;
    private String password;
    private String phonenumber;
    private String name;
}
