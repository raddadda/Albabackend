package com.jobstore.jobstore.service;

import com.jobstore.jobstore.dto.AttendanceDto;
import com.jobstore.jobstore.entity.Attendance;
import com.jobstore.jobstore.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;

//    public void createAttendance(AttendanceDto attendanceDto){
//
//        Attendance attendance = new Attendance();
//        attendance.setStart(attendanceDto.getStart());
//        attendance.setEnd(attendanceDto.getEnd());
//        attendance.setGowork(attendanceDto.getGowork());
//        attendance.setLeavework(attendanceDto.getLeavework());
//        attendance.setRegistertime(attendanceDto.getRegistertime());
//
//        attendanceRepository.save(attendance);
//    }
}
