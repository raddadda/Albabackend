package com.jobstore.jobstore.dto.response.attendance;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceHistoryDto<T> {
    private List<T> content;
    private int size;
    private int number;
    private int totalPages;
    private boolean hasNext;
}
