package com.jobstore.jobstore.controller.Attendance;

import com.amazonaws.services.kms.model.NotFoundException;
import com.jobstore.jobstore.dto.request.attendance.AttendanceDto;
import com.jobstore.jobstore.dto.request.attendance.AttendanceUpdateDto;
import com.jobstore.jobstore.dto.response.attendance.AttendanceHistoryDto;
import com.jobstore.jobstore.dto.response.ResultDto;
import com.jobstore.jobstore.entity.Attendance;
import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.entity.Payment;
import com.jobstore.jobstore.service.AttendanceService;
import com.jobstore.jobstore.service.MemberService;
import com.jobstore.jobstore.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
     * 근태 조회 및 페이지네이션
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
        try{
            Member member = memberService.findMemberid(memberid);
            if(member == null || !member.getRole().equals("ADMIN")){
                return ResponseEntity.ok(ResultDto.of("실패", "권한이 맞지 않습니다.", null));
            }

            return ResponseEntity.ok(ResultDto.of("성공", "조회성공", attendanceService.getAttendancesByMemberId("ADMIN", memberid, storeid, page)));
        }catch (HttpClientErrorException.MethodNotAllowed e){
            throw new HttpClientErrorException(HttpStatus.METHOD_NOT_ALLOWED,e.getMessage());
        }
        catch (HttpClientErrorException e){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
        catch (NotFoundException e){
            throw new NotFoundException(e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
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
        try {
            System.out.println("-----------------getHistoryUser-----------------");
            Member member = memberService.findMemberid(memberid);
            if(member == null || !member.getRole().equals("USER")){
                return ResponseEntity.ok(ResultDto.of("실패", "권한이 맞지 않습니다.", null));
            }

            return ResponseEntity.ok(ResultDto.of("성공", "조회성공", attendanceService.getAttendancesByMemberId("USER", memberid, storeid, page)));
        }catch (HttpClientErrorException.MethodNotAllowed e){
            throw new HttpClientErrorException(HttpStatus.METHOD_NOT_ALLOWED,e.getMessage());
        }
        catch (HttpClientErrorException e){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
        catch (NotFoundException e){
            throw new NotFoundException(e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * admin의 user 지표 조회
     */
    @GetMapping("/admin/attendance/data/{memberid}/{storeid}")
    @Operation(summary = "출결 조회 데이터", description = "admin 알바생 출결 조회")
    @Parameter(name = "memberid", description = "memberid", required = true)
    @Parameter(name = "storeid", description = "storeid", required = true)
    @ResponseBody
    public ResponseEntity<ResultDto<HashMap<String,Long>>> getUserData(
            @PathVariable(value = "memberid", required = true) String memberid,
            @PathVariable(value = "storeid", required = true) Long storeid
    ) {
        try {
            System.out.println("-----------------getUserData-----------------");
            Member member = memberService.findMemberid(memberid);
            if(member == null || !member.getRole().equals("ADMIN")){
                return ResponseEntity.ok(ResultDto.of("실패", "권한이 맞지 않습니다.", null));
            }
            HashMap<String,Long> getAttendData = attendanceService.getAttendData(member.getMemberid());
            if(getAttendData == null){
                return ResponseEntity.ok(ResultDto.of("null", "user지표가 null값입니다.", getAttendData));
            }
            return ResponseEntity.ok(ResultDto.of("성공", "조회성공", getAttendData));
        }catch (HttpClientErrorException.MethodNotAllowed e){
            throw new HttpClientErrorException(HttpStatus.METHOD_NOT_ALLOWED,e.getMessage());
        }
        catch (HttpClientErrorException e){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
        catch (NotFoundException e){
            throw new NotFoundException(e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 이번달 일한 시간
     */
    @GetMapping("/user/attendance/month/{memberid}/{storeid}")
    @Operation(summary = "user 이번달 일한시간", description = "user가 이번달에 일한시간을 조회")
    @Parameter(name = "memberid", description = "memberid", required = true)
    @Parameter(name = "storeid", description = "storeid", required = true)
    public ResponseEntity<ResultDto<Long>> getWeek(
            @PathVariable(value = "memberid", required = true) String memberid,
            @PathVariable(value = "storeid", required = true) Long storeid) {
        try {
            System.out.println("-----------------getWeekuser-----------------");
            Member member = memberService.findMemberid(memberid);
            if(member == null || !member.getRole().equals("USER")){
                return ResponseEntity.ok(ResultDto.of("실패", "권한이 맞지 않습니다.", null));
            }
            long nowmonth = attendanceService.localDateTimeToMonth(LocalDateTime.now());
            long month = attendanceService.workMonth(nowmonth, member.getMemberid());

            return ResponseEntity.ok(ResultDto.of("성공", "주급 월급 조회성공", month));
        }catch (HttpClientErrorException.MethodNotAllowed e){
            throw new HttpClientErrorException(HttpStatus.METHOD_NOT_ALLOWED,e.getMessage());
        }
        catch (HttpClientErrorException e){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
        catch (NotFoundException e){
            throw new NotFoundException(e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
    @GetMapping("/admin/attendance/month/{memberid}/{storeid}/{worker}")
    @Operation(summary = "admin 월 지표", description = "admin이 해당 wokrer가 이번달에 일한시간을 조회")
    @Parameter(name = "memberid", description = "memberid", required = true)
    @Parameter(name = "storeid", description = "storeid", required = true)
    @Parameter(name = "worker", description = "worker", required = true)
    public ResponseEntity<ResultDto<Long>> getWeek(
            @PathVariable(value = "memberid", required = true) String memberid,
            @PathVariable(value = "storeid", required = true) Long storeid,
            @PathVariable(value = "worker", required = true) String worker) {
        try {
            System.out.println("-----------------getWeekadmin-----------------");
            Member member = memberService.findMemberid(memberid);
            if(member == null || !member.getRole().equals("ADMIN")){
                return ResponseEntity.ok(ResultDto.of("실패", "권한이 맞지 않습니다.", null));
            }
            long nowmonth = attendanceService.localDateTimeToMonth(LocalDateTime.now());
            long month = attendanceService.workMonth(nowmonth, worker);

            return ResponseEntity.ok(ResultDto.of("성공", "주급 월급 조회성공", month));
        }catch (HttpClientErrorException.MethodNotAllowed e){
            throw new HttpClientErrorException(HttpStatus.METHOD_NOT_ALLOWED,e.getMessage());
        }
        catch (HttpClientErrorException e){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
        catch (NotFoundException e){
            throw new NotFoundException(e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 이번주 일한 시간
     */
    @GetMapping("/user/attendance/week/{memberid}/{storeid}")
    @Operation(summary = "user 이번주 일한시간", description = "user가 이번주에 일한시간을 조회")
    @Parameter(name = "memberid", description = "memberid", required = true)
    @Parameter(name = "storeid", description = "storeid", required = true)
    public ResponseEntity<ResultDto<Long>> getMonth(
            @PathVariable(value = "memberid", required = true) String memberid,
            @PathVariable(value = "storeid", required = true) Long storeid) {
        try {
            System.out.println("-----------------getMonth-----------------");
            Member member = memberService.findMemberid(memberid);
            if(member == null || !member.getRole().equals("USER")){
                return ResponseEntity.ok(ResultDto.of("실패", "권한이 맞지 않습니다.", null));
            }
            long localDateTimeToWeek = attendanceService.localDateTimeToWeek(member.getMemberid());

            return ResponseEntity.ok(ResultDto.of("성공", "주급 월급 조회성공", localDateTimeToWeek));
        }catch (HttpClientErrorException.MethodNotAllowed e){
            throw new HttpClientErrorException(HttpStatus.METHOD_NOT_ALLOWED,e.getMessage());
        }
        catch (HttpClientErrorException e){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
        catch (NotFoundException e){
            throw new NotFoundException(e.getMessage());
        }
        catch (Exception e){

            throw new RuntimeException(e.getMessage());
        }
    }
    @GetMapping("/admin/attendance/week/{memberid}/{storeid}/{worker}")
    @Operation(summary = "admin 주간 지표", description = "admin이 해당 worker의 이번주에 일한시간을 조회")
    @Parameter(name = "memberid", description = "memberid", required = true)
    @Parameter(name = "storeid", description = "storeid", required = true)
    @Parameter(name = "worker", description = "worker", required = true)
    public ResponseEntity<ResultDto<Long>> getMonthAdmin(
            @PathVariable(value = "memberid", required = true) String memberid,
            @PathVariable(value = "storeid", required = true) Long storeid,
             @PathVariable(value = "worker", required = true) String worker
    ) {
        try{
            System.out.println("-----------------getMonthAdmin-----------------");
            if (!memberService.findByMemberidToRole(memberid).equals("ADMIN")) {         //권한 확인
                return ResponseEntity.ok(ResultDto.of("실패", "권한이 맞지 않습니다.", null));
            }
            long localDateTimeToWeek = attendanceService.localDateTimeToWeek(worker);
            return ResponseEntity.ok(ResultDto.of("성공", "주급 월급 조회성공", localDateTimeToWeek));
        }catch (HttpClientErrorException.MethodNotAllowed e){
            throw new HttpClientErrorException(HttpStatus.METHOD_NOT_ALLOWED,e.getMessage());
        }
        catch (HttpClientErrorException e){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
        catch (NotFoundException e){
            throw new NotFoundException(e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
    /**
     * 이번달과 저번달간의 일한시간 퍼센트 지표
     */
    @GetMapping("/user/attendance/percent/{memberid}/{storeid}")
    @Operation(summary = "user percent조회", description = "user의 저번달과 이번달간의 일한 시간 차이를 계산하는 percent조회")
    @Parameter(name = "memberid", description = "memberid", required = true)
    @Parameter(name = "storeid", description = "storeid", required = true)
    public ResponseEntity<ResultDto<Double>> getPercent(
            @PathVariable(value = "memberid", required = true) String memberid,
            @PathVariable(value = "storeid", required = true) Long storeid) {
        try {
            System.out.println("-----------------getPercent-----------------");
            Member member = memberService.findMemberid(memberid);
            if(member == null || !member.getRole().equals("USER")){
                return ResponseEntity.ok(ResultDto.of("실패", "권한이 맞지 않습니다.", null));
            }
            double result = attendanceService.workPercent(member.getMemberid());
            if (result != -1) {
                return ResponseEntity.ok(ResultDto.of("성공", "퍼센트 조회성공", result));
            }
            return ResponseEntity.ok(ResultDto.of("실패", "퍼센트 조회실패", null));
        }catch (HttpClientErrorException.MethodNotAllowed e){
            throw new HttpClientErrorException(HttpStatus.METHOD_NOT_ALLOWED,e.getMessage());
        }
        catch (HttpClientErrorException e){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
        catch (NotFoundException e){
            throw new NotFoundException(e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
    @GetMapping("/admin/attendance/percent/{memberid}/{storeid}/{worker}")
    @Operation(summary = "admin percent조회", description = "admin이 해당 worker의 저번달과 이번달간의 일한 시간 차이를 percent로 조회")
    @Parameter(name = "memberid", description = "memberid", required = true)
    @Parameter(name = "storeid", description = "storeid", required = true)
    @Parameter(name = "worker", description = "worker", required = true)
    public ResponseEntity<ResultDto<Double>> getPercentAdmin(
            @PathVariable(value = "memberid", required = true) String memberid,
            @PathVariable(value = "storeid", required = true) Long storeid,
            @PathVariable(value = "worker", required = true) String worker) {
        try {
            System.out.println("-----------------getPercentAdmin-----------------");
            if (!memberService.findByMemberidToRole(memberid).equals("ADMIN")) {   //권한 확인
                return ResponseEntity.ok(ResultDto.of("실패", "권한이 맞지 않습니다.", null));
            }
            double result = attendanceService.workPercent(worker);
            if (result != -1) {
                return ResponseEntity.ok(ResultDto.of("성공", "퍼센트 조회성공", result));
            }
            return ResponseEntity.ok(ResultDto.of("실패", "퍼센트 조회실패", null));
        }catch (HttpClientErrorException.MethodNotAllowed e){
            throw new HttpClientErrorException(HttpStatus.METHOD_NOT_ALLOWED,e.getMessage());
        }
        catch (HttpClientErrorException e){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
        catch (NotFoundException e){
            throw new NotFoundException(e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/user/attendance/data/{memberid}/{storeid}")
    @Operation(summary = "user 월간 그래프", description = "이번달을 기준으로 최근 5달의 일한시간의 합을 값을 조회한다.")
    @Parameter(name = "memberid", description = "memberid", required = true)
    @Parameter(name = "storeid", description = "storeid", required = true)
    public ResponseEntity<ResultDto<Map<Long,Long>>> getUserMonthData(
            @PathVariable(value = "memberid", required = true) String memberid,
            @PathVariable(value = "storeid", required = true) Long storeid) {
        try {
            System.out.println("-----------------getUserMonthData-----------------");
            Member member = memberService.findMemberid(memberid);
            if(member == null || !member.getRole().equals("USER")){
                return ResponseEntity.ok(ResultDto.of("실패", "권한이 맞지 않습니다.", null));
            }
            Map<Long,Long> result = attendanceService.getUserMonthData(member);
            if (result != null) {
                return ResponseEntity.ok(ResultDto.of("성공", "5달 데이터 조회 성공", result));
            }
            return ResponseEntity.ok(ResultDto.of("null", "조회가 null값입니다.", result));
        }catch (HttpClientErrorException.MethodNotAllowed e){
            throw new HttpClientErrorException(HttpStatus.METHOD_NOT_ALLOWED,e.getMessage());
        }
        catch (HttpClientErrorException e){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
        catch (NotFoundException e){
            throw new NotFoundException(e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }


    }

    /**
     * 근태 CRUD
     */
    @PostMapping("/admin/attendance/create")
    @Operation(summary = "admin 근태추가", description = "admin 근태생성")
    public ResponseEntity<ResultDto<Object>> createAttendance(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema = @Schema(implementation = AttendanceDto.class)))
            @RequestBody AttendanceDto attendanceDto
            //, @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        try {
            System.out.println("-----------------createAttendance-----------------");
            String role = memberService.findByMemberidToRole(attendanceDto.getMemberid()); //권한 확인
            if (!role.equals("ADMIN")) {
                return ResponseEntity.ok(ResultDto.of("실패", "admin 권한이 아닙니다.", null));
            }
            boolean result = attendanceService.createAttendance(attendanceDto);
            if (!result) {
                return ResponseEntity.ok(ResultDto.of("실패", "result를 가져오는데 실패했습니다.", null));
            }
            return ResponseEntity.ok(ResultDto.of("성공", "근태추가", attendanceDto));
        }catch (HttpClientErrorException.MethodNotAllowed e){
            throw new HttpClientErrorException(HttpStatus.METHOD_NOT_ALLOWED,e.getMessage());
        }
        catch (HttpClientErrorException e){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
        catch (NotFoundException e){
            throw new NotFoundException(e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }

    @PatchMapping("/user/attendance/gowork")
    @Operation(summary = "user 출근시간등록", description = "user가 출근했을때 클릭하는곳입니다.")
    @ResponseBody
    public ResponseEntity<ResultDto<Object>> goworkAttendance(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema = @Schema(implementation = AttendanceUpdateDto.class)))

            @RequestBody AttendanceUpdateDto attendanceUpdateDto
    ) {
        try {
            System.out.println("-----------------goworkAttendance-----------------");
            //권한 확인
            String role = memberService.findByMemberidToRole(attendanceUpdateDto.getWorker());

            if (!role.equals("USER")) {
                return ResponseEntity.ok(ResultDto.of("실패", "user권한이 아닙니다.", null));
            }
            boolean result = attendanceService.goworkAttendance(attendanceUpdateDto);
            if (!result) {
                return ResponseEntity.ok(ResultDto.of("실패", "result를 가져오는데 실패했습니다.", null));
            }
            return ResponseEntity.ok(ResultDto.of("성공", "출근", attendanceUpdateDto));
        }catch (HttpClientErrorException.MethodNotAllowed e){
            throw new HttpClientErrorException(HttpStatus.METHOD_NOT_ALLOWED,e.getMessage());
        }
        catch (HttpClientErrorException e){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
        catch (NotFoundException e){
            throw new NotFoundException(e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @PatchMapping("/user/attendance/leavework")
    @Operation(summary = "user 퇴근시간등록", description = "user가 퇴근했을때 클릭하는 곳이며 동시에 오늘의 급여가 payment에 반영됩니다.")
    @ResponseBody
    public ResponseEntity<ResultDto<Object>> leaveworkAttendance(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema = @Schema(implementation = AttendanceUpdateDto.class)))

            @RequestBody AttendanceUpdateDto attendanceUpdateDto
    ) {
        try {
            System.out.println("-----------------leaveworkAttendance-----------------");
            String role = memberService.findByMemberidToRole(attendanceUpdateDto.getWorker());
            //권한 확인
            if (!role.equals("USER")) {
                return ResponseEntity.ok(ResultDto.of("실패", "user권한이 아닙니다.", null));
            }
            attendanceService.leaveworkAttendance(attendanceUpdateDto);
            //출근 확인
            if (!attendanceService.existGoworkCheck(attendanceUpdateDto)) {
                return ResponseEntity.ok(ResultDto.of("실패", "오늘의 출근기록이 없습니다.", null));
            }

            return ResponseEntity.ok(ResultDto.of("성공", "퇴근 및 오늘 급여수당 추가 성공", null));
        }catch (HttpClientErrorException.MethodNotAllowed e){
            throw new HttpClientErrorException(HttpStatus.METHOD_NOT_ALLOWED,e.getMessage());
        }
        catch (HttpClientErrorException e){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
        catch (NotFoundException e){
            throw new NotFoundException(e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/admin/attendance/findAll")
    @Operation(summary = "admin 모든 근태기록 조회", description = "admin이 모든 user들에대한 근태기록을 볼 수 있는 곳입니다.")
    @ResponseBody
    public ResponseEntity<ResultDto<Object>> findAllAttendance(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema = @Schema(implementation = AttendanceDto.class)))
            @RequestBody AttendanceDto attendanceDto
    ) {
        try{
            System.out.println("-----------------findAllAttendance-----------------");
            String role = memberService.findByMemberidToRole(attendanceDto.getMemberid());
            if (role.equals("ADMIN")) {
                List<Attendance> attendance = attendanceService.findAllAttendance();
                return ResponseEntity.ok(ResultDto.of("성공", "admin 근태 조회", attendance));
            }
            return ResponseEntity.ok(ResultDto.of("실패", "admin 권한이 아닙니다.", null));
        }catch (HttpClientErrorException.MethodNotAllowed e){
            throw new HttpClientErrorException(HttpStatus.METHOD_NOT_ALLOWED,e.getMessage());
        }
        catch (HttpClientErrorException e){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
        catch (NotFoundException e){
            throw new NotFoundException(e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @PatchMapping("/admin/attendance/update")
    @Operation(summary = "admin 근태업데이트", description = "admin이 근태를 업데이트하는 곳입니다.")
    @ResponseBody
    public ResponseEntity<ResultDto<Object>> updateAttendance(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema = @Schema(implementation = AttendanceUpdateDto.class)))
            @RequestBody AttendanceUpdateDto attendanceUpdateDto
    ) {
        try {
            System.out.println("-----------------updateAttendance-----------------");
            String role = memberService.findByMemberidToRole(attendanceUpdateDto.getMemberid());
            if (role.equals("ADMIN")) {
                boolean result = attendanceService.updateAttendance(attendanceUpdateDto);
                if (!result) {
                    return ResponseEntity.ok(ResultDto.of("실패", "result를 업데이트하는데 실패했습니다.", null));
                }


                return ResponseEntity.ok(ResultDto.of("성공", "admin 근태 업데이트", attendanceUpdateDto));
            }
            return ResponseEntity.ok(ResultDto.of("실패", "admin권한이 아닙니다.", null));
        }catch (HttpClientErrorException.MethodNotAllowed e){
            throw new HttpClientErrorException(HttpStatus.METHOD_NOT_ALLOWED,e.getMessage());
        }
        catch (HttpClientErrorException e){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
        catch (NotFoundException e){
            throw new NotFoundException(e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @PatchMapping("/admin/attendance/delete")
    @Operation(summary = "admin 근태삭제", description = "admin이 근태를 삭제하는 곳입니다.")
    @ResponseBody
    public ResponseEntity<ResultDto<Object>> deleteAttendance(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema = @Schema(implementation = AttendanceUpdateDto.class)))
            @RequestBody AttendanceUpdateDto attendanceUpdateDto
    ) {
        try {
            System.out.println("-----------------deleteAttendance-----------------");
            String role = memberService.findByMemberidToRole(attendanceUpdateDto.getMemberid());
            if (role.equals("ADMIN")) {
                boolean result = attendanceService.deleteAttendance(attendanceUpdateDto);

                if (!result) {
                    return ResponseEntity.ok(ResultDto.of("실패", "result를 삭제하는데 실패했습니다.", null));
                }
                return ResponseEntity.ok(ResultDto.of("성공", "근태 삭제 성공", null));
            }
            return ResponseEntity.ok(ResultDto.of("실패", "admin권한이 아닙니다.", null));
        }catch (HttpClientErrorException.MethodNotAllowed e){
            throw new HttpClientErrorException(HttpStatus.METHOD_NOT_ALLOWED,e.getMessage());
        }
        catch (HttpClientErrorException e){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
        catch (NotFoundException e){
            throw new NotFoundException(e.getMessage());
        }
        catch (Exception e){
            throw  new RuntimeException(e.getMessage());
        }
    }


//    @GetMapping("/admin/attendance/workerlist/{memberid}/{storeid}")
//    @Operation(summary = "worker리스트 조회", description = "admin의 worker리스트 조회")
//    @Parameter(name = "memberid", description = "memberid", required = true)
//    @Parameter(name = "storeid", description = "storeid", required = true)
//    @ResponseBody
//    public ResponseEntity<ResultDto<HashMap<String,String>>> getWorkerList(
//            @PathVariable(value = "memberid", required = true) String memberid,
//            @PathVariable(value = "storeid", required = true) Long storeid
//    ) {
//        System.out.println("-----------------getUserData-----------------");
//        Member member = memberService.findMemberid(memberid);
//        if(member == null || !member.getRole().equals("ADMIN")){
//            return ResponseEntity.ok(ResultDto.of("실패", "권한이 맞지 않습니다.", null));
//        }
//        HashMap<String,String> result  = memberService.findByWorker(storeid);
//        System.out.println("@@@");
//        if(result == null){
//            return ResponseEntity.ok(ResultDto.of("null", "worker리스트가 null값입니다.", result));
//        }
//        return ResponseEntity.ok(ResultDto.of("성공", "조회성공", result));
//    }

    @PatchMapping("/admin/attendance/confirm")
    @Operation(summary = "admin 근태승인", description = "admin이 근태를 승인하는 곳이며 급여가 반영됩니다.")
    @ResponseBody
    public ResponseEntity<ResultDto<Object>> confirmAttendance(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema = @Schema(implementation = AttendanceUpdateDto.class)))
            @RequestBody AttendanceUpdateDto attendanceUpdateDto
    ) {
        try {
            System.out.println("-----------------confirmAttendance-----------------");
            Member member = memberService.findMemberid(attendanceUpdateDto.getMemberid());
            if(member == null || !member.getRole().equals("ADMIN")){
                return ResponseEntity.ok(ResultDto.of("실패", "권한이 맞지 않습니다.", null));
            }

//            String role = memberService.findByMemberidToRole(attendanceUpdateDto.getMemberid());
//            System.out.println("0");
//            if (role.equals("ADMIN")) {
            System.out.println("1");
            AttendanceUpdateDto result = attendanceService.confirmAttendance(attendanceUpdateDto,member);
            if (result ==null) {
                return ResponseEntity.ok(ResultDto.of("실패", "result를 받아오는데 실패했습니다.", null));
            }
            System.out.println("2"+result.getWage());
            Attendance attendance = attendanceService.findByWorkderAndAttendid(attendanceUpdateDto.getWorker(),attendanceUpdateDto.getAttendid());
            long payCalculate = attendanceService.payCalculate(result,member,attendance);
            System.out.println("payCalculate"+payCalculate);
            if (payCalculate == -1) {
                return ResponseEntity.ok(ResultDto.of("실패", "급여 계산에 실패했습니다.", null));
            }
            System.out.println(" attendanceUpdateDto.getLeavework():"+ result.getLeavework());
            Payment payment = paymentService.addPaymentForMember(result.getWorker(), result.getLeavework(), payCalculate);
            if(payment == null){
                return ResponseEntity.ok(ResultDto.of("실패", "급여 추가에 실패했습니다.", null));
            }
            return ResponseEntity.ok(ResultDto.of("성공", "근태 승인 및 급여 추가 성공", null));
//            }
//            return ResponseEntity.ok(ResultDto.of("실패", "admin권한이 아닙니다.", null));
        }catch (HttpClientErrorException.MethodNotAllowed e){
            throw new HttpClientErrorException(HttpStatus.METHOD_NOT_ALLOWED,e.getMessage());
        }
        catch (HttpClientErrorException e){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
        catch (NotFoundException e){
            throw new NotFoundException(e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

}
