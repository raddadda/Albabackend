package com.jobstore.jobstore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDto {
    private long attendid;
    private String start;
    private String end;
    private String registertime;
}
