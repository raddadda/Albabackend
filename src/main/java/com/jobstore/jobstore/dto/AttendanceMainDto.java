package com.jobstore.jobstore.dto;


import com.jobstore.jobstore.entity.Attendance;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceMainDto {
    private long maxcursor;
    private List<Attendance> list;
}
