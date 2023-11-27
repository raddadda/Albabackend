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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    @Operation(summary = "Payment api", description = "payment api입니다.")
    @ResponseBody
    public ResponseEntity<ResultDto<Payment>> addPayment(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
            content = @Content(schema=@Schema(implementation = PaymentinsertDto.class)))
                                                             @RequestBody PaymentinsertDto paymentinsertDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("----------:"+paymentinsertDto.getMemberid()+paymentinsertDto.getPay()+paymentinsertDto.getRegister());
        System.out.println(memberService.findByMemberidToRole(paymentinsertDto.getMemberid()));
        if(memberService.findByMemberidToRole(paymentinsertDto.getMemberid()).equals("USER")) {
            Payment paydata = paymentService.addPaymentForMember(paymentinsertDto.getMemberid(), paymentinsertDto.getRegister(), paymentinsertDto.getPay());
            if (paydata != null) {
                return ResponseEntity.ok(ResultDto.of("200", "급여정보삽입완료", paydata));
            }
        }
        return ResponseEntity.ok(ResultDto.of("200", "실패", null));

//        String memberid=principalDetails.getMember().getMemberid();
//        if(paymentinsertDto.getMemberid().equals(memberid)){
//            if(memberService.findByMemberidToRole(memberid).equals("User")){
//                Payment paydata=paymentService.addPaymentForMember(paymentinsertDto.getMemberid(), paymentinsertDto.getRegister(), paymentinsertDto.getPay());
//                if(paydata != null){
//                    return ResponseEntity.ok(ResultDto.of("resultcode","급여정보삽입완료",paydata));
//                }
//            }else if(memberService.findByMemberidToRole(memberid).equals("Admin")){
//                return ResponseEntity.ok(ResultDto.of("resultcode","실패",null));
//            }
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
//    @PostMapping("/allpayment")
//    @ResponseBody
//    public ResponseEntity<ResultDto<List<PaymentDto>>> findmember_forPayment(@RequestBody Member member){
//        List<PaymentDto> paymentDtos = paymentService.findMemberid_ForAllPayment(member.getMemberid());
//        if(paymentDtos!=null){
//           return ResponseEntity.ok(ResultDto.of("resultcode","유저에대한 조회 성공",paymentDtos));
//        }
//        return ResponseEntity.ok(ResultDto.of("resultcode","조회실패",null));
//    }
@GetMapping("/admin/findall/{memberid}/{page}")
@Operation(summary = "Payment api", description = "admin전체 조회 api")
@Parameter(name = "memberid", description = "memberid", required = true)
@Parameter(name = "page", description = "페이지 번호 0부터 시작", required = true)
@ResponseBody
public ResponseEntity<ResultDto<PaymentPagenationDto>> findAdminAllpayment(@PathVariable String memberid, @PathVariable Integer page){
    return ResponseEntity.ok(ResultDto.of("200","성공",paymentService.findByMemberidAdmin(memberid,page)));
}
@GetMapping("/user/findall/{memberid}/{page}")
@Operation(summary = "Payment api", description = "User전체 조회 api")
@Parameter(name = "memberid", description = "memberid", required = true)
@Parameter(name = "page", description = "페이지 번호 0부터 시작", required = true)
public ResponseEntity<ResultDto<PaymentPagenationDto>> findAllmember(@PathVariable String memberid,@PathVariable Integer page){
    return ResponseEntity.ok(ResultDto.of("200","성공",paymentService.findByMemberidUser(memberid,page)));
}

    @PostMapping("/allpayment")
    @Operation(summary = "Payment api", description = "payment api입니다.")
    @ResponseBody
    public ResponseEntity<ResultDto<PaymentMainDto>> paymentMain(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
            content = @Content(schema=@Schema(implementation = PaymentinsertDto.class)))
                                                           @RequestBody PaymentAllPaymentDto paymentAllPaymentDto){
        //   2023-11-20T12:00:00
        long month= paymentService.localDateTimeToMonth(paymentAllPaymentDto.getTime());

        //한달급여
        Long payment = paymentService.paymentMain(month);
        //주급
        long week = paymentService.localDateTimeToWeek(paymentAllPaymentDto.getTime(),paymentAllPaymentDto.getMemberid());

        PaymentMainDto paymentMainDto = new PaymentMainDto();
        return ResponseEntity.ok(ResultDto.of("200","조회성공",new PaymentMainDto(payment,week)));
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
    @Operation(summary = "Payment api", description = "저번달 대비 퍼센트 증감률")
    @ResponseBody
    public ResponseEntity<ResultDto<Double>> last_recentpercentage(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = PaymentPercentageDto.class)))@RequestBody PaymentPercentageDto paymentPercentageDto){
        return ResponseEntity.ok(ResultDto.of("200","성공",paymentService.last_recentpercentage(paymentPercentageDto.getMemberid(),paymentPercentageDto.getMonth())));

    }
    @PostMapping("/admin/percent")
    @Operation(summary = "Payment api",description = "Admin 저번달 대비 퍼센트 증감률")
    @ResponseBody
    public ResponseEntity<ResultDto<Double>> last_recentAdminPercentage(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
            content = @Content(schema=@Schema(implementation = PaymentPercentageDto.class)))@RequestBody PaymentPercentageDto paymentPercentageDto){
            return ResponseEntity.ok(ResultDto.of("200","성공",paymentService.last_recentAdminPercentage(paymentPercentageDto.getMemberid(),paymentPercentageDto.getMonth())));
    }

    @PostMapping("/admin/allpayment")
    @ResponseBody
    public ResponseEntity<ResultDto<Long>> adminAllPay(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = PaymentAdminAllpayment.class)))@RequestBody PaymentAdminAllpayment paymentAdminAllpayment){
//        System.out.println("asdsadasddssadsa:sadasdas"+memberid);
        return ResponseEntity.ok(ResultDto.of("200","월중전체 지출액 성공",paymentService.addPaymentForAdmin(paymentAdminAllpayment.getMemberid(),paymentAdminAllpayment.getMonth())));
    }
    @GetMapping("/admin/each/{memberid}/{month}")
    @Operation(summary = "Payment api", description = "Admin에서 유저별 월급 계산후 반환")
    @ResponseBody
    public ResponseEntity<ResultDto<Map<String,Long>>> abcde(@PathVariable String memberid,@PathVariable long month){
        return ResponseEntity.ok(ResultDto.of("200","스토어에 속해있는 전체 멤버별 월급리스트맵 반환",paymentService.AdminfindEachMemberPay(memberid,month)));
    }
}




