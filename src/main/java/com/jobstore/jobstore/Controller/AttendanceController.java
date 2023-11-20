package com.jobstore.jobstore.Controller;

import com.jobstore.jobstore.dto.AttendanceDto;
import com.jobstore.jobstore.dto.request.AdminjoinDto;
import com.jobstore.jobstore.dto.response.ResultDto;
import com.jobstore.jobstore.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;

//    @PostMapping("/admin/attendance")
//    public ResponseEntity<ResultDto<Object>> joinAdmin(
//            @RequestBody AttendanceDto attendanceDto
//    ) {
//        attendanceService.createAttendance(attendanceDto);
//        return ResponseEntity.ok(ResultDto.of("resultCode","회원가입이 실패했습니다.", null));
//    }

}
