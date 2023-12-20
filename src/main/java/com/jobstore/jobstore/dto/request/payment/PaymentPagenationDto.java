package com.jobstore.jobstore.dto.request.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentPagenationDto<T> {
    private List<T> content;
    private int size;
    private int number;
    private int totalPages;
    private boolean hasNext;
}
