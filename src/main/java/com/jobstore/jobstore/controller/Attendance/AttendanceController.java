package com.jobstore.jobstore.controller.Attendance;

import com.jobstore.jobstore.dto.request.attendance.AttendanceDto;
import com.jobstore.jobstore.dto.request.attendance.AttendanceUpdateDto;
import com.jobstore.jobstore.dto.response.attendance.AttendanceHistoryDto;
import com.jobstore.jobstore.dto.response.ResultDto;
import com.jobstore.jobstore.entity.Attendance;
import com.jobstore.jobstore.service.AttendanceService;
import com.jobstore.jobstore.service.MemberService;
import com.jobstore.jobstore.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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


    /**
     근태 조회 및 페이지네이션
     */
    @GetMapping("/admin/attendance/{memberid}/{storeid}/{page}")
    @Operation(summary = "admin history", description = "admin history조회")
    @Parameter(name = "memberid", description = "memberid", required = true)
    @Parameter(name = "storeid", description = "storeid", required = true)
    @Parameter(name = "page", description = "페이지 번호 0부터 시작", required = false)
    @ResponseBody
    public ResponseEntity<ResultDto<AttendanceHistoryDto>> getHistoryAdmin(
            @PathVariable(value = "memberid", required = true) String memberid,
            @PathVariable(value = "storeid", required = true) Long storeid,
            @PathVariable(value = "page", required = false) Integer page
    ) {
        if(!memberService.findByMemberidToRole(memberid).equals("ADMIN")){  //권한 확인
            return ResponseEntity.ok(ResultDto.of("실패","권한이 맞지 않습니다.",null));
        }
        return ResponseEntity.ok(ResultDto.of("성공","조회성공",attendanceService.getAttendancesByMemberId("ADMIN",memberid,storeid,page)));

    }
    @GetMapping("/user/attendance/{memberid}/{storeid}/{page}")
    @Operation(summary = "user history", description = "user history조회")
    @Parameter(name = "memberid", description = "memberid", required = true)
    @Parameter(name = "storeid", description = "storeid", required = true)
    @Parameter(name = "page", description = "페이지 번호 0부터 시작", required = false)
    public ResponseEntity<ResultDto<AttendanceHistoryDto>> getHistoryUser(
            @PathVariable(value = "memberid", required = true) String memberid,
            @PathVariable(value = "storeid", required = true) Long storeid,
            @PathVariable(value = "page", required = false) Integer page
    ) {
        if(!memberService.findByMemberidToRole(memberid).equals("USER")){   //권한 확인
            return ResponseEntity.ok(ResultDto.of("실패","권한이 맞지 않습니다.",null));
        }
        return ResponseEntity.ok(ResultDto.of("성공","조회성공",attendanceService.getAttendancesByMemberId("USER",memberid,storeid,page)));
    }
    /**
     이번달 일한 시간
     */
    @GetMapping("/user/attendance/month/{memberid}/{storeid}")
    @Operation(summary = "user 이번달 일한시간", description = "user가 이번달에 일한시간을 조회")
    @Parameter(name = "memberid", description = "memberid", required = true)
    @Parameter(name = "storeid", description = "storeid", required = true)
    public ResponseEntity<ResultDto<Long>> getWeek(
            @PathVariable(value = "memberid", required = true) String memberid,
            @PathVariable(value = "storeid", required = true) Long storeid){
        if(!memberService.findByMemberidToRole(memberid).equals("USER")){   //권한 확인
            return ResponseEntity.ok(ResultDto.of("실패","권한이 맞지 않습니다.",null));
        }
        long nowmonth = attendanceService.localDateTimeToMonth(LocalDateTime.now());
        long month = attendanceService.workMonth(nowmonth,memberid);

        return ResponseEntity.ok(ResultDto.of("성공","주급 월급 조회성공",month));
    }
    /**
     이번주 일한 시간
     */
    @GetMapping("/user/attendance/week/{memberid}/{storeid}")
    @Operation(summary = "user 이번주 일한시간", description = "user가 이번주에 일한시간을 조회")
    @Parameter(name = "memberid", description = "memberid", required = true)
    @Parameter(name = "storeid", description = "storeid", required = true)
    public ResponseEntity<ResultDto<Long>> getMonth(
            @PathVariable(value = "memberid", required = true) String memberid,
            @PathVariable(value = "storeid", required = true) Long storeid){

        if(!memberService.findByMemberidToRole(memberid).equals("USER")){         //권한 확인
            return ResponseEntity.ok(ResultDto.of("실패","권한이 맞지 않습니다.",null));
        }
        long localDateTimeToWeek = attendanceService.localDateTimeToWeek(memberid);

        return ResponseEntity.ok(ResultDto.of("성공","주급 월급 조회성공",localDateTimeToWeek));
    }
    /**
     이번달과 저번달간의 일한시간 퍼센트 지표
     */
    @GetMapping("/user/attendance/percent/{memberid}/{storeid}")
    @Operation(summary = "user percent조회", description = "user의 저번달과 이번달간의 일한 시간 차이를 계산하는 percent조회")
    @Parameter(name = "memberid", description = "memberid", required = true)
    @Parameter(name = "storeid", description = "storeid", required = true)
    public ResponseEntity<ResultDto<Double>> getPercent(
            @PathVariable(value = "memberid", required = true) String memberid,
            @PathVariable(value = "storeid", required = true) Long storeid){
        if(!memberService.findByMemberidToRole(memberid).equals("USER")){   //권한 확인
            return ResponseEntity.ok(ResultDto.of("실패","권한이 맞지 않습니다.",null));
        }
        double result = attendanceService.workPercent(memberid);
        if(result != -1){
            return ResponseEntity.ok(ResultDto.of("성공","퍼센트 조회성공",result));
        }
        return ResponseEntity.ok(ResultDto.of("실패","퍼센트 조회실패",null));
    }

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
        String role = memberService.findByMemberidToRole(attendanceDto.getMemberid()); //권한 확인
        if(!role.equals("ADMIN")){
            return ResponseEntity.ok(ResultDto.of("실패","admin 권한이 아닙니다.", null));
        }
        boolean result = attendanceService.createAttendance(attendanceDto);
        if(!result){
            return ResponseEntity.ok(ResultDto.of("실패","result를 가져오는데 실패했습니다.", null));
        }
        return ResponseEntity.ok(ResultDto.of("성공","근태추가", attendanceDto));

    }

    @PatchMapping("/user/attendance/gowork")
    @Operation(summary = "user 출근시간등록", description = "user가 출근했을때 클릭하는곳입니다.")
    @ResponseBody
    public ResponseEntity<ResultDto<Object>> goworkAttendance(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = AttendanceUpdateDto.class)))

            @RequestBody AttendanceUpdateDto attendanceUpdateDto
    ) {
        //권한 확인
//        String role = memberService.findByMemberidToRole(attendanceUpdateDto.getMemberid());
//        if(!role.equals("USER")){
//            return ResponseEntity.ok(ResultDto.of("실패","user권한이 아닙니다.", null));
//        }
        boolean result = attendanceService.goworkAttendance(attendanceUpdateDto);
        if(!result){
            return ResponseEntity.ok(ResultDto.of("실패","result를 가져오는데 실패했습니다.", null));
        }
        return ResponseEntity.ok(ResultDto.of("성공","출근", attendanceUpdateDto));
    }

    @PatchMapping("/user/attendance/leavework")
    @Operation(summary = "user 퇴근시간등록", description = "user가 퇴근했을때 클릭하는 곳이며 동시에 오늘의 급여가 payment에 반영됩니다.")
    @ResponseBody
    public ResponseEntity<ResultDto<Object>> leaveworkAttendance(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = AttendanceUpdateDto.class)))

            @RequestBody AttendanceUpdateDto attendanceUpdateDto
    ) {
        String role = memberService.findByMemberidToRole(attendanceUpdateDto.getMemberid());
        //권한 확인
//        if(!role.equals("USER")){
//            return ResponseEntity.ok(ResultDto.of("실패","user권한이 아닙니다.", null));
//        }
        attendanceService.leaveworkAttendance(attendanceUpdateDto);
        //출근 확인
        if(!attendanceService.existGoworkCheck(attendanceUpdateDto)){
            return ResponseEntity.ok(ResultDto.of("실패","오늘의 출근기록이 없습니다.", null));
        }
        long result = attendanceService.payCalculate(attendanceUpdateDto);
        if(result==-1){
            return ResponseEntity.ok(ResultDto.of("실패","조회에 실패했습니다.", null));
        }
        paymentService.addPaymentForMember(attendanceUpdateDto.getMemberid(),attendanceUpdateDto.getLeavework(),result);

        return ResponseEntity.ok(ResultDto.of("성공","퇴근 및 오늘 급여수당 추가 성공", result));
    }
    @PostMapping("/admin/attendance/findAll")
    @Operation(summary = "admin 모든 근태기록 조회", description = "admin이 모든 user들에대한 근태기록을 볼 수 있는 곳입니다.")
    @ResponseBody
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


    @PatchMapping("/admin/attendance/update")
    @Operation(summary = "admin 근태업데이트", description = "admin이 근태를 업데이트하는 곳입니다.")
    @ResponseBody
    public ResponseEntity<ResultDto<Object>> updateAttendance(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = AttendanceUpdateDto.class)))
            @RequestBody AttendanceUpdateDto attendanceUpdateDto
    ) {
        String role = memberService.findByMemberidToRole(attendanceUpdateDto.getMemberid());
        if(role.equals("ADMIN")){
            boolean result = attendanceService.updateAttendance(attendanceUpdateDto);
            if(!result){
                return ResponseEntity.ok(ResultDto.of("실패","result를 업데이트하는데 실패했습니다.", null));
            }
            return ResponseEntity.ok(ResultDto.of("성공","admin 근태 업데이트", attendanceUpdateDto));
        }
        return ResponseEntity.ok(ResultDto.of("실패","admin권한이 아닙니다.", null));
    }
    @PatchMapping("/admin/attendance/delete")
    @Operation(summary = "admin 근태삭제", description = "admin이 근태를 삭제하는 곳입니다.")
    @ResponseBody
    public ResponseEntity<ResultDto<Object>> deleteAttendance(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = AttendanceUpdateDto.class)))
            @RequestBody AttendanceUpdateDto attendanceUpdateDto
    ) {
        String role = memberService.findByMemberidToRole(attendanceUpdateDto.getMemberid());
        if(role.equals("ADMIN")){
            boolean result = attendanceService.deleteAttendance(attendanceUpdateDto);
            if(!result){
                return ResponseEntity.ok(ResultDto.of("실패","result를 삭제하는데 실패했습니다.", null));
            }
            return ResponseEntity.ok(ResultDto.of("성공","근태 삭제 성공", null));
        }
        return ResponseEntity.ok(ResultDto.of("실패","admin권한이 아닙니다.", null));
    }

}
