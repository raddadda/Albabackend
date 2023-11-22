package com.jobstore.jobstore.controller;

import com.jobstore.jobstore.config.PrincipalDetails;
import com.jobstore.jobstore.dto.request.PaymentAllPaymentDto;
import com.jobstore.jobstore.dto.request.PaymentinsertDto;
import com.jobstore.jobstore.dto.response.ResultDto;
import com.jobstore.jobstore.entity.Payment;
import com.jobstore.jobstore.repository.MemberRepository;
import com.jobstore.jobstore.service.MemberService;
import com.jobstore.jobstore.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;


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
                return ResponseEntity.ok(ResultDto.of("resultcode", "급여정보삽입완료", paydata));
            }
        }
        return ResponseEntity.ok(ResultDto.of("resultcode", "실패", null));

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

    @PostMapping("/allpayment")
    @Operation(summary = "Payment api", description = "payment api입니다.")
    @ResponseBody
    public ResponseEntity<ResultDto<Long>> paymentMain(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
            content = @Content(schema=@Schema(implementation = PaymentinsertDto.class)))
                                                           @RequestBody PaymentAllPaymentDto paymentAllPaymentDto){
        //   2023-11-20T12:00:00
        long month= paymentService.localDateTimeToMonth(paymentAllPaymentDto.getTime());
        Long payment = paymentService.paymentMain(month);
        return ResponseEntity.ok(ResultDto.of("resultcode","조회실패",payment));
    }
}




