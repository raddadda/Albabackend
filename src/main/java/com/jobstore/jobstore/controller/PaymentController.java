package com.jobstore.jobstore.controller;

import com.jobstore.jobstore.config.PrincipalDetails;
import com.jobstore.jobstore.dto.request.payment.*;
import com.jobstore.jobstore.dto.response.ResultDto;
import com.jobstore.jobstore.entity.Payment;
import com.jobstore.jobstore.repository.MemberRepository;
import com.jobstore.jobstore.service.MemberService;
import com.jobstore.jobstore.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;


@Controller
@Tag(name = "Payment", description = "Payment CRUD")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @PostMapping("/add")
    @Operation(summary = "Payment api", description = "근태정보에서 급여 정보 반환 (이건굳이 안써도됨) 일급데이터 삽입")
    @ResponseBody
    public ResponseEntity<ResultDto<Payment>> addPayment(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
            content = @Content(schema=@Schema(implementation = PaymentinsertDto.class)))
                                                             @RequestBody PaymentinsertDto paymentinsertDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (memberService.findByMemberidToRole(paymentinsertDto.getMemberid()).equals("USER")) {
            Payment paydata = paymentService.addPaymentForMember(paymentinsertDto.getMemberid(), paymentinsertDto.getRegister(), paymentinsertDto.getPay());
            if (paydata != null) {
                return ResponseEntity.ok(ResultDto.of("200", "급여정보삽입완료", paydata));
            }
        }
        return ResponseEntity.ok(ResultDto.of("200", "실패", null));

    }
@GetMapping("/admin/findall/{memberid}/{page}")
@Operation(summary = "Payment api", description = "admin전체 조회 api(history와 데이터 그래프에서 사용)")
@Parameter(name = "memberid", description = "memberid", required = true)
@Parameter(name = "page", description = "페이지 번호 0부터 시작", required = true)
@ResponseBody
public ResponseEntity<ResultDto<PaymentPagenationDto>> findAdminAllpayment(@PathVariable String memberid, @PathVariable Integer page){
        try{
            return ResponseEntity.ok(ResultDto.of("200","성공",paymentService.findByMemberidAdmin(memberid,page)));
        }catch(Exception e){
            throw new RuntimeException("admin 전제조회 실패"+e.getMessage());
        }
}
@GetMapping("/user/findall/{memberid}/{page}")
@Operation(summary = "Payment api", description = "User전체 조회 api(history와 데이터 그래프에서 사용)")
@Parameter(name = "memberid", description = "memberid", required = true)
@Parameter(name = "page", description = "페이지 번호 0부터 시작", required = true)
public ResponseEntity<ResultDto<PaymentPagenationDto>> findAllmember(@PathVariable String memberid,@PathVariable Integer page){
        try {
            return ResponseEntity.ok(ResultDto.of("200","성공",paymentService.findByMemberidUser(memberid,page)));
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
}
@GetMapping("/user/findAll/{memberid}/{month}")
@Operation(summary = "Payment api", description = "User전체 조회 api(데이터 그래프에서 사용)")
public ResponseEntity<ResultDto<Map<Long,Long>>> findAllmember(@PathVariable String memberid,@PathVariable long month){
        try{
            return ResponseEntity.ok(ResultDto.of("200","성공",paymentService.userAllLists(memberid,month)));
        }catch (Exception e){
            throw new RuntimeException("유저 전체조회 실패"+e.getMessage());
        }
    }
    @PostMapping("/allpayment")
    @Operation(summary = "Payment api", description = "payment api입니다.(User에 관한 월급과 주급 반환)")
    @ResponseBody
    public ResponseEntity<ResultDto<PaymentMainDto>> paymentMain(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
            content = @Content(schema=@Schema(implementation = PaymentAllPaymentDto.class)))
                                                           @RequestBody PaymentAllPaymentDto paymentAllPaymentDto){
        try {
            LocalDateTime time = LocalDateTime.now();
            long month= paymentService.localDateTimeToMonth(time);

            //한달급여
            Long payment = paymentService.paymentMain(time.getMonthValue());
            //주급
            long week = paymentService.localDateTimeToWeek(time,paymentAllPaymentDto.getMemberid());

            PaymentMainDto paymentMainDto = new PaymentMainDto();
            return ResponseEntity.ok(ResultDto.of("200","조회성공",new PaymentMainDto(payment,week)));

        }catch (Exception e){
            throw new RuntimeException("PaymentMain 처리 중 오류 발생: " + e.getMessage());
        }
    }

//    @PostMapping("/payment")
//    @Operation(summary = "Payment api", description = "payment api입니다.")
//    @ResponseBody
//    public ResponseEntity<ResultDto<Long>> paymentMain(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
//            content = @Content(schema=@Schema(implementation = PaymentinsertDto.class)))
//                                                       @RequestBody PaymentAllPaymentDto paymentAllPaymentDto){
//        //   2023-11-20T12:00:00
//        long month= paymentService.localDateTimeToMonth(paymentAllPaymentDto.getTime());
//
//        long week = paymentService.localDateTimeToWeek(paymentAllPaymentDto.getTime(),paymentAllPaymentDto.getMemberid());
//
//        //System.out.println("month:"+month);
//        Long payment = paymentService.paymentMain(month);
//        //System.out.println("payment:"+payment);
//        return ResponseEntity.ok(ResultDto.of("resultcode","조회성공",payment));
//    }
    @PostMapping("/percent")
    @Operation(summary = "Payment api", description = "저번달 대비 퍼센트 증감률(User)")
    @ResponseBody
    public ResponseEntity<ResultDto<Double>> last_recentpercentage(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = PaymentPercentageDto.class)))@RequestBody PaymentPercentageDto paymentPercentageDto){
        try{
            return ResponseEntity.ok(ResultDto.of("200","성공",paymentService.last_recentpercentage(paymentPercentageDto.getMemberid(),paymentPercentageDto.getMonth())));
        }catch (Exception e){
            throw new RuntimeException("percent반환 실패(USER)"+e.getMessage());
        }

    }
    @PostMapping("/admin/percent")
    @Operation(summary = "Payment api",description = "Admin 저번달 대비 퍼센트 증감률(Admin)")
    @ResponseBody
    public ResponseEntity<ResultDto<Double>> last_recentAdminPercentage(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
            content = @Content(schema=@Schema(implementation = PaymentPercentageDto.class)))@RequestBody PaymentPercentageDto paymentPercentageDto){
        try {
            double percent=paymentService.last_recentAdminPercentage(paymentPercentageDto.getMemberid(),paymentPercentageDto.getMonth());
            return ResponseEntity.ok(ResultDto.of("200","성공",percent));
        }catch (Exception e) {
            throw new RuntimeException("Admin 퍼센트 반환 실패"+e.getMessage());
        }

    }

    @PostMapping("/admin/allpayment")
    @Operation(summary = "Payment api",description = "해당달 지출액 정보 삽입")
    @ResponseBody
    public ResponseEntity<ResultDto<Long>> adminAllPayCreate(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = PaymentAdminAllpayment.class)))@RequestBody PaymentAdminAllpayment paymentAdminAllpayment){
        try {

            return ResponseEntity.ok(ResultDto.of("200","월중전체 지출액 삽입성공",paymentService.insertPaymentForAdmin(paymentAdminAllpayment.getMemberid(),paymentAdminAllpayment.getMonth())));
        }catch (Exception e){
            throw new RuntimeException("Admin월중 전체 지출액"+e.getMessage());
        }
    }
    @GetMapping("/admin/allpayment/{memberid}/{month}")
    @Operation(summary = "Payment api",description = "해당달 지출액 정보 조회")
    @ResponseBody
    public ResponseEntity<ResultDto<Long>> adminAllPayfind(@PathVariable("memberid") String memberid,@PathVariable("month") long month){
        return ResponseEntity.ok(ResultDto.of("200","월중 전체 지출액 조회 성공",paymentService.getPaymentForAdmin(memberid,month)));

    }
    @GetMapping("/admin/each/{memberid}/{month}")
    @Operation(summary = "Payment api", description = "Admin에서 유저별 월급 계산후 반환(그 가게에 속한 사람들 memberid별 월급 계산후 반환 <리스트>)")
    @ResponseBody
    public ResponseEntity<ResultDto<Map<String,Long>>> abcde(@PathVariable String memberid,@PathVariable long month){
        try {
            return ResponseEntity.ok(ResultDto.of("200","스토어에 속해있는 전체 멤버별 월급리스트맵 반환",paymentService.AdminfindEachMemberPay(memberid,month)));
        } catch (Exception e){
            throw new RuntimeException("User별 월급반환실패(Admin)"+e.getMessage());
        }
    }
}




