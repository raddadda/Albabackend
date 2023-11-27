package com.jobstore.jobstore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    private long payid;
    private long pay;
    private long month;
    private LocalDateTime register;
    private Map<Long, Long> userListMap;

    public PaymentDto(Map<Long, Long> longLongMap) {
    }

    public void setUserListMap(Map<Long, Long> userlistmap) {
        this.userListMap = userListMap;
    }
}
