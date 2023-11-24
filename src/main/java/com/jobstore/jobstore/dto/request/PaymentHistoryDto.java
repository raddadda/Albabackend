package com.jobstore.jobstore.dto.request;

import com.jobstore.jobstore.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentHistoryDto {
    private List<Payment> payments;
    private int maxcursor;
}
