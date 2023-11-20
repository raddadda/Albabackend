package com.jobstore.jobstore.service;

import com.jobstore.jobstore.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;
}
