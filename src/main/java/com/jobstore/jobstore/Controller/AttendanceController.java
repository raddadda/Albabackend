package com.jobstore.jobstore.Controller;

import com.jobstore.jobstore.config.PrincipalDetails;
import com.jobstore.jobstore.dto.Attendance.*;
import com.jobstore.jobstore.dto.response.ResultDto;
import com.jobstore.jobstore.entity.Attendance;
import com.jobstore.jobstore.service.AttendanceService;
import com.jobstore.jobstore.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private PaymentService paymentService;


    @PostMapping("/admin/attendance")
    @Operation(summary = "admin 근태추가", description = "admin 근태생성")
    public ResponseEntity<ResultDto<Object>> createAttendance(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = AttendanceDto.class)))
            @RequestBody AttendanceDto attendanceDto
            //, @AuthenticationPrincipal PrincipalDetails principalDetails
     ) {
        //String userId = principalDetails.getMember().getMemberid();
        attendanceService.createAttendance(attendanceDto);
        return ResponseEntity.ok(ResultDto.of("resultCode","[admin]출퇴근시간 기록 성공", null));
    }

    @PostMapping("/user/attendance/gowork")
    @Operation(summary = "user 출근시간등록", description = "user가 출근했을때 클릭하는곳입니다.")
    public ResponseEntity<ResultDto<Object>> goworkAttendance(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = AttendanceDto.class)))

            @RequestBody AttendanceDto attendanceDto
    ) {
        System.out.println("goworkAttendance");
        attendanceService.goworkAttendance(attendanceDto);
        return ResponseEntity.ok(ResultDto.of("resultCode","[user]출퇴근시간 기록 성공", null));
    }

    @PostMapping("/user/attendance/leavework")
    @Operation(summary = "user 퇴근시간등록", description = "user가 퇴근했을때 클릭하는 곳이며 동시에 오늘의 급여가 payment에 반영됩니다.")
    public ResponseEntity<ResultDto<Object>> leaveworkAttendance(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = AttendanceDto.class)))

            @RequestBody AttendanceDto attendanceDto
    ) {
        attendanceService.leaveworkAttendance(attendanceDto);
        //오늘 급여
        long result = attendanceService.payCalculate(attendanceDto);
        if(result!=-1){
            System.out.println("attendanceDto.getMemberid()"+attendanceDto.getMemberid());
            paymentService.addPaymentForMember(attendanceDto.getMemberid(),attendanceDto.getLeavework(),result);
            return ResponseEntity.ok(ResultDto.of("[user]출퇴근시간 기록 성공","오늘 급여수당 추가 성공", result));
        }
        return ResponseEntity.ok(ResultDto.of("[user]출퇴근시간 기록 실패","member가 존재하지 않습니다.", null));
    }
    @PostMapping("/admin/attendance/findAll")
    @Operation(summary = "admin 모든 근태기록 조회", description = "admin이 모든 user들에대한 근태기록을 볼 수 있는 곳입니다.")
    public ResponseEntity<ResultDto<Object>> findAllAttendance(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = AttendanceDto.class)))
            @RequestBody AttendanceDto attendanceDto
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
    @Operation(summary = "admin 근태업데이트", description = "admin이 근태를 업데이트하는 곳입니다.")
    public ResponseEntity<ResultDto<Object>> updateAttendance(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = AttendanceDto.class)))
            @RequestBody AttendanceDto attendanceDto
    ) {
        attendanceService.updateAttendance(attendanceDto);
        return ResponseEntity.ok(ResultDto.of("resultCode","[admin]출퇴근시간 수정 성공", null));
    }
    @PostMapping("/admin/attendance/delete")
    @Operation(summary = "admin 근태삭제", description = "admin이 근태를 삭제하는 곳입니다.")
    public ResponseEntity<ResultDto<Object>> deleteAttendance(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = AttendanceDto.class)))
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
