package com.jobstore.jobstore.controller;

import com.jobstore.jobstore.dto.Attendance.*;
import com.jobstore.jobstore.dto.response.ResultDto;
import com.jobstore.jobstore.entity.Attendance;
import com.jobstore.jobstore.service.AttendanceService;
import com.jobstore.jobstore.service.MemberService;
import com.jobstore.jobstore.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@Tag(name = "Attendance", description = "Attendance CRUD")
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private MemberService memberService;

    @PostMapping("/admin/attendance")
    @Operation(summary = "admin 근태추가", description = "admin 근태생성")
    public ResponseEntity<ResultDto<Object>> createAttendance(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = AttendanceDto.class)))
            @RequestBody AttendanceDto attendanceDto
            //, @AuthenticationPrincipal PrincipalDetails principalDetails
     ) {
        //String userId = principalDetails.getMember().getMemberid();
        String role = memberService.findByMemberidToRole(attendanceDto.getMemberid());
        if(role.equals("ADMIN")){
            attendanceService.createAttendance(attendanceDto);
            return ResponseEntity.ok(ResultDto.of("성공","근태추가", attendanceDto));
        }
        return ResponseEntity.ok(ResultDto.of("실패","admin 권한이 아닙니다.", null));

    }

    @PostMapping("/user/attendance/gowork")
    @Operation(summary = "user 출근시간등록", description = "user가 출근했을때 클릭하는곳입니다.")
    public ResponseEntity<ResultDto<Object>> goworkAttendance(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = AttendanceUpdateDto.class)))

            @RequestBody AttendanceUpdateDto attendanceUpdateDto
    ) {
        System.out.println("111111");
        String role = memberService.findByMemberidToRole(attendanceUpdateDto.getMemberid());
        System.out.println("2222222");
        if(role.equals("USER")){
            System.out.println("333333");
            attendanceService.goworkAttendance(attendanceUpdateDto);
            System.out.println("444444");
            return ResponseEntity.ok(ResultDto.of("성공","출근", attendanceUpdateDto));
        }
        return ResponseEntity.ok(ResultDto.of("실패","user권한이 아닙니다.", null));
    }

    @PostMapping("/user/attendance/leavework")
    @Operation(summary = "user 퇴근시간등록", description = "user가 퇴근했을때 클릭하는 곳이며 동시에 오늘의 급여가 payment에 반영됩니다.")
    public ResponseEntity<ResultDto<Object>> leaveworkAttendance(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = AttendanceUpdateDto.class)))

            @RequestBody AttendanceUpdateDto attendanceUpdateDto
    ) {
        String role = memberService.findByMemberidToRole(attendanceUpdateDto.getMemberid());
        //권한 확인
        if(!role.equals("USER")){
            return ResponseEntity.ok(ResultDto.of("실패","user권한이 아닙니다.", null));
        }
        attendanceService.leaveworkAttendance(attendanceUpdateDto);
        //출근 확인
        if(!attendanceService.existGoworkCheck(attendanceUpdateDto)){
            return ResponseEntity.ok(ResultDto.of("실패","오늘의 출근기록이 없습니다.", null));
        }
        long result = attendanceService.payCalculate(attendanceUpdateDto);
        //멤버확인
        if(result==-1){
            return ResponseEntity.ok(ResultDto.of("실패","member가 존재하지 않습니다.", null));
        }
        paymentService.addPaymentForMember(attendanceUpdateDto.getMemberid(),attendanceUpdateDto.getLeavework(),result);
        return ResponseEntity.ok(ResultDto.of("성공","퇴근 및 오늘 급여수당 추가 성공", result));
    }
    @PostMapping("/admin/attendance/findAll")
    @Operation(summary = "admin 모든 근태기록 조회", description = "admin이 모든 user들에대한 근태기록을 볼 수 있는 곳입니다.")
    public ResponseEntity<ResultDto<Object>> findAllAttendance(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = AttendanceDto.class)))
            @RequestBody AttendanceDto attendanceDto
    ) {
        String role = memberService.findByMemberidToRole(attendanceDto.getMemberid());
        if(role.equals("ADMIN")){
            List<Attendance> attendance = attendanceService.findAllAttendance();
            return ResponseEntity.ok(ResultDto.of("성공","admin 근태 조회",attendance));
        }
        return ResponseEntity.ok(ResultDto.of("실패","admin 권한이 아닙니다.",null));
    }

    @PostMapping("/admin/attendance/update")
    @Operation(summary = "admin 근태업데이트", description = "admin이 근태를 업데이트하는 곳입니다.")
    public ResponseEntity<ResultDto<Object>> updateAttendance(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = AttendanceUpdateDto.class)))
            @RequestBody AttendanceUpdateDto attendanceUpdateDto
    ) {
        String role = memberService.findByMemberidToRole(attendanceUpdateDto.getMemberid());
        if(role.equals("ADMIN")){
            attendanceService.updateAttendance(attendanceUpdateDto);
            return ResponseEntity.ok(ResultDto.of("성공","admin 근태 업데이트", attendanceUpdateDto));
        }
        return ResponseEntity.ok(ResultDto.of("실패","admin권한이 아닙니다.", null));
    }
    @PostMapping("/admin/attendance/delete")
    @Operation(summary = "admin 근태삭제", description = "admin이 근태를 삭제하는 곳입니다.")
    public ResponseEntity<ResultDto<Object>> deleteAttendance(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = AttendanceUpdateDto.class)))
            @RequestBody AttendanceUpdateDto attendanceUpdateDto
    ) {
        String role = memberService.findByMemberidToRole(attendanceUpdateDto.getMemberid());
        if(role.equals("ADMIN")){
            attendanceService.deleteAttendance(attendanceUpdateDto);
            return ResponseEntity.ok(ResultDto.of("성공","근태 삭제 성공", null));
        }
        return ResponseEntity.ok(ResultDto.of("실패","admin권한이 아닙니다.", null));

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
