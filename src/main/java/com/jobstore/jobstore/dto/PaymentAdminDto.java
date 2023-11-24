package com.jobstore.jobstore.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentAdminDto {
    private String memberid;
    private long storeid;
    private long month;
    private long sum;
}
