package com.jobstore.jobstore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDto {
    private String adminid;
    private String password;
    private String phonenumber;
    private String name;
    private String role;
    private String companynumber;
    private String ceo;
}
