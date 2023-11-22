package com.jobstore.jobstore.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentAllPaymentDto {
    private String memberid;
    private LocalDateTime time;
}
