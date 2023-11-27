package com.jobstore.jobstore.controller;

import com.jobstore.jobstore.dto.Attendance.*;
import com.jobstore.jobstore.dto.AttendanceHistoryDto;
import com.jobstore.jobstore.dto.AttendanceMainDto;
import com.jobstore.jobstore.dto.response.ResultDto;
import com.jobstore.jobstore.entity.Attendance;
import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.entity.Payment;
import com.jobstore.jobstore.service.AttendanceService;
import com.jobstore.jobstore.service.MemberService;
import com.jobstore.jobstore.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
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


//    @GetMapping("/admin/attendance/{attendid}")
//    public ResponseEntity<ResultDto<Slice<Attendance>>> getAttendances(@PathVariable Long attendid) {
//        int pageSize = 3; // 페이지당 아이템 수
//        Pageable pageable = PageRequest.of(0, pageSize); // 첫 번째 페이지
//        return ResponseEntity.ok(ResultDto.of("실패","admin 권한이 아닙니다.",attendanceService.getAttendancesById(attendid, pageable)));
//    }

    /**
     근태 조회 및 페이지네이션
     */
    @GetMapping("/admin/attendance/{memberid}/{storeid}/{page}")
@Operation(summary = "admin history", description = "admin history조회")
public ResponseEntity<ResultDto<AttendanceHistoryDto>> getHistoryAdmin(@PathVariable String memberid, @PathVariable Long storeid
        , @PathVariable(required = false) Integer page
) {
    return ResponseEntity.ok(ResultDto.of("성공","조회성공",attendanceService.getAttendancesByMemberId("ADMIN",memberid,storeid,page)));
}
@GetMapping("/user/attendance/{memberid}/{storeid}/{page}")
@Operation(summary = "user history", description = "user history조회")
public ResponseEntity<ResultDto<AttendanceHistoryDto>> getHistoryUser(@PathVariable String memberid, @PathVariable Long storeid
        , @PathVariable(required = false) Integer page
) {
    return ResponseEntity.ok(ResultDto.of("성공","조회성공",attendanceService.getAttendancesByMemberId("USER",memberid,storeid,page)));
}
    /**
     이번달 일한 시간
     */
@GetMapping("/user/attendance/month/{memberid}/{storeid}")
@Operation(summary = "user 이번달 일한시간", description = "user가 이번달에 일한시간을 조회")
public ResponseEntity<ResultDto<Long>> getWeek(@PathVariable String memberid, @PathVariable Long storeid
){
   // LocalDateTime time = LocalDateTime.now();
   // long week = attendanceService.workCalculate();
   // long month = 11;
    long nowmonth = attendanceService.localDateTimeToMonth(LocalDateTime.now());
    long month = attendanceService.workMonth(nowmonth,memberid);
    //System.out.println("weekWork:");
   // System.out.println("weekWork:"+month);
    //LocalDateTime time;
//    long localDateTimeToWeek = attendanceService.localDateTimeToWeek(memberid);
//    long thisWeek = paymentService.localDateTimeToWeek(time,memberid);
//    long week = attendanceService.workWeek(thisWeek,memberid);
    return ResponseEntity.ok(ResultDto.of("성공","주급 월급 조회성공",month));
}
    /**
     이번주 일한 시간
     */
    @GetMapping("/user/attendance/week/{memberid}/{storeid}")
    @Operation(summary = "user 이번주 일한시간", description = "user가 이번주에 일한시간을 조회")
    public ResponseEntity<ResultDto<Long>> getMonth(@PathVariable String memberid, @PathVariable Long storeid
    ){
        // LocalDateTime time = LocalDateTime.now();
        // long week = attendanceService.workCalculate();
        // long month = 11;

        //System.out.println("weekWork:");
       // System.out.println("weekWork:"+month);
        //LocalDateTime time;
        long localDateTimeToWeek = attendanceService.localDateTimeToWeek(memberid);
//    long thisWeek = paymentService.localDateTimeToWeek(time,memberid);
//    long week = attendanceService.workWeek(thisWeek,memberid);
        return ResponseEntity.ok(ResultDto.of("성공","주급 월급 조회성공",localDateTimeToWeek));
    }
    /**
     이번달과 저번달간의 일한시간 퍼센트 지표
     */
@GetMapping("/user/attendance/percent/{memberid}/{storeid}")
@Operation(summary = "user percent조회", description = "user의 저번달과 이번달간의 일한 시간 차이를 계산하는 percent조회")
public ResponseEntity<ResultDto<Long>> getPercent(@PathVariable String memberid, @PathVariable Long storeid
){
    double result = attendanceService.workPercent(memberid);
    System.out.println("result: "+result);
    if(result != -1){
        return ResponseEntity.ok(ResultDto.of("성공","퍼센트 조회성공",null));
    }
    return ResponseEntity.ok(ResultDto.of("실패","퍼센트 조회실패",null));
}
//    @GetMapping("/admin/attendance")
//    public Page<Payment> getPayments(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size
//    ) {
//        PageRequest pageRequest = PageRequest.of(page, size);
//        return paymentRepository.findAll(pageRequest);
//    }
//    @PostMapping("/admin/attendance")
//    @Operation(summary = "admin 메인화면", description = "월급,주급,히스토리")
//    public ResponseEntity<ResultDto<Object>> adminAttendanceMain(
//            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
//                    content = @Content(schema=@Schema(implementation = AttendanceDto.class)))
//            @RequestBody AttendanceDto attendanceDto
//    ) {
//        String role = memberService.findByMemberidToRole(attendanceDto.getMemberid());
//        if(role.equals("ADMIN")){
//            List<Attendance> attendance = attendanceService.findAllAttendance();
//            return ResponseEntity.ok(ResultDto.of("성공","admin 근태 조회",attendance));
//        }
//        return ResponseEntity.ok(ResultDto.of("실패","admin 권한이 아닙니다.",null));
//    }
//
//    @PostMapping("/user/attendance")
//    @Operation(summary = "user 메인화면", description = "admin이 모든 user들에대한 근태기록을 볼 수 있는 곳입니다.")
//    public ResponseEntity<ResultDto<Object>> userAttendanceMain(
//            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
//                    content = @Content(schema=@Schema(implementation = AttendanceDto.class)))
//            @RequestBody AttendanceDto attendanceDto
//    ) {
//        String role = memberService.findByMemberidToRole(attendanceDto.getMemberid());
//        if(role.equals("ADMIN")){
//            List<Attendance> attendance = attendanceService.findAllAttendance();
//            return ResponseEntity.ok(ResultDto.of("성공","admin 근태 조회",attendance));
//        }
//        return ResponseEntity.ok(ResultDto.of("실패","admin 권한이 아닙니다.",null));
//    }
    /**
     근태 CRUD
     */
    @PostMapping("/admin/attendance/create")
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
       // long month = 11;
       // long weekWork = attendanceService.wageWeek(month);

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
