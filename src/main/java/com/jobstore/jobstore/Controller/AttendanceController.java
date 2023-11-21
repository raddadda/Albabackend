package com.jobstore.jobstore.Controller;

import com.jobstore.jobstore.dto.AttendanceDto;
import com.jobstore.jobstore.dto.request.AdminjoinDto;
import com.jobstore.jobstore.dto.response.ResultDto;
import com.jobstore.jobstore.entity.Attendance;
import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.repository.PaymentRepository;
import com.jobstore.jobstore.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private PaymentRepository paymentRepository;

    @PostMapping("/admin/attendance")
    public ResponseEntity<ResultDto<Object>> createAttendance(
            @RequestBody AttendanceDto attendanceDto
            //, Authentication auth
     ) {
        attendanceService.createAttendance(attendanceDto);
        return ResponseEntity.ok(ResultDto.of("resultCode","[admin]출퇴근시간 기록 성공", null));
    }

    @PostMapping("/user/attendance/gowork")
    public ResponseEntity<ResultDto<Object>> goworkAttendance(
            @RequestBody AttendanceDto attendanceDto
    ) {
        System.out.println("goworkAttendance");
        attendanceService.goworkAttendance(attendanceDto);
        return ResponseEntity.ok(ResultDto.of("resultCode","[user]출퇴근시간 기록 성공", null));
    }

    @PostMapping("/user/attendance/leavework")
    public ResponseEntity<ResultDto<Object>> leaveworkAttendance(
            @RequestBody AttendanceDto attendanceDto
    ) {
        attendanceService.leaveworkAttendance(attendanceDto);
        long result = attendanceService.payCalculate(attendanceDto);
        if(result!=-1){
            //paymentRepository.addPaymentForMember(attendanceDto.getMemberid());
            return ResponseEntity.ok(ResultDto.of("[user]출퇴근시간 기록 성공","오늘 급여수당 추가 성공", result));
        }
        return ResponseEntity.ok(ResultDto.of("[user]출퇴근시간 기록 실패","member가 존재하지 않습니다.", null));
    }
    @PostMapping("/admin/attendance/findAll")
    public ResponseEntity<ResultDto<Object>> findAllAttendance(
           // @RequestBody AttendanceDto attendanceDto
    ) {
//        Member member = memberService.findMember(id);
//        Attendance attendanceAll = member.getBasket();
        List<Attendance> attendance = attendanceService.findAllAttendance();


//        for(Attendance attendance1 : attendance){
//            System.out.println("dasdasd:"+attendance1.getAttendid());
//        }

        return ResponseEntity.ok(ResultDto.of("resultCode","[admin]출퇴근시간 조회 성공",attendance));
    }

    @PostMapping("/admin/attendance/update")
    public ResponseEntity<ResultDto<Object>> updateAttendance(
            @RequestBody AttendanceDto attendanceDto
    ) {
        attendanceService.updateAttendance(attendanceDto);
        return ResponseEntity.ok(ResultDto.of("resultCode","[admin]출퇴근시간 수정 성공", null));
    }
    @PostMapping("/admin/attendance/delete")
    public ResponseEntity<ResultDto<Object>> deleteAttendance(
            @RequestBody AttendanceDto attendanceDto
    ) {
        attendanceService.deleteAttendance(attendanceDto);
        return ResponseEntity.ok(ResultDto.of("resultCode","[admin]출퇴근시간 삭제 성공", null));
    }

//    @PostMapping("/admin/attendance")
//    public ResponseEntity<ResultDto<Object>> createAttendance(
//            @RequestBody AttendanceDto attendanceDto
//            //, Authentication auth
//    ) {
//        attendanceService.createAttendance(attendanceDto);
//        return ResponseEntity.ok(ResultDto.of("resultCode","[admin]출퇴근시간 기록 성공", null));
//    }

}
