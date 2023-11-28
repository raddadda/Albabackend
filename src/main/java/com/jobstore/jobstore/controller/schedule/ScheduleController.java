package com.jobstore.jobstore.controller.schedule;

import com.jobstore.jobstore.dto.response.ResultDto;
import com.jobstore.jobstore.dto.response.attendance.AttendanceHistoryDto;
import com.jobstore.jobstore.entity.Attendance;
import com.jobstore.jobstore.repository.AttendanceRepository;
import com.jobstore.jobstore.service.AttendanceService;
import com.jobstore.jobstore.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ScheduleController {
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/admin/schedule/{memberid}/{storeid}/{month}")
    @Operation(summary = "admin schedule", description = "admin 스케줄 조회")
    @Parameter(name = "memberid", description = "memberid", required = true)
    @Parameter(name = "storeid", description = "storeid", required = true)
    @Parameter(name = "month", description = "이번달 month", required = false)
    @ResponseBody
    public ResponseEntity<ResultDto<List<Attendance>>> adminMonthSchedule(
            @PathVariable(value = "memberid", required = true) String memberid,
            @PathVariable(value = "storeid", required = true) Long storeid,
            @PathVariable(value = "month", required = false) String time
    ) {
        LocalDateTime localDateTime =  LocalDateTime.parse(time);
        List<Attendance> attendanceList = scheduleService.thisMonthSchedule(localDateTime,memberid,"ADMIN");
        if(attendanceList== null){
            return ResponseEntity.ok(ResultDto.of("null", "이번달 스케줄값이 null입니다", attendanceList));
        }
        return ResponseEntity.ok(ResultDto.of("성공", "이번달 스케줄 조회성공", attendanceList));
    }

    @GetMapping("/user/schedule/{memberid}/{storeid}/{month}")
    @Operation(summary = "user schedule", description = "user 스케줄 조회")
    @Parameter(name = "memberid", description = "memberid", required = true)
    @Parameter(name = "storeid", description = "storeid", required = true)
    @Parameter(name = "time", description = "이번달 month", required = false)
    @ResponseBody
    public ResponseEntity<ResultDto<List<Attendance>>> userMonthSchedule(
            @PathVariable(value = "memberid", required = true) String memberid,
            @PathVariable(value = "storeid", required = true) Long storeid,
            @PathVariable(value = "month", required = false) String time
    ) {
        LocalDateTime localDateTime =  LocalDateTime.parse(time);
        List<Attendance> attendanceList = scheduleService.thisMonthSchedule(localDateTime,memberid,"ADMIN");
        if(attendanceList== null){
            return ResponseEntity.ok(ResultDto.of("null", "이번달 스케줄값이 null입니다", attendanceList));
        }
        return ResponseEntity.ok(ResultDto.of("성공", "이번달 스케줄 조회성공", attendanceList));
    }
}
