package com.jobstore.jobstore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreDto {
    private long storeid;
    private String companyname;
    private String ceo;
    private String companynumber;
    private String companyimg;
    private String invitecode;
}
