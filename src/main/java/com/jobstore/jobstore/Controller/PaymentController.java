package com.jobstore.jobstore.Controller;

import com.jobstore.jobstore.dto.LoginDto;
import com.jobstore.jobstore.dto.MemberDto;
import com.jobstore.jobstore.dto.PaymentDto;
import com.jobstore.jobstore.dto.request.PaymentinsertDto;
import com.jobstore.jobstore.dto.response.ResultDto;
import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.entity.Payment;
import com.jobstore.jobstore.repository.MemberRepository;
import com.jobstore.jobstore.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.List;


@Controller
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private MemberRepository memberRepository;

    @PostMapping("/add")
    @Operation(summary = "Payment api", description = "payment api입니다.")
    @ResponseBody
    public ResponseEntity<ResultDto<Payment>> addPayment(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
            content = @Content(schema=@Schema(implementation = PaymentinsertDto.class)))
                                                             @RequestBody PaymentinsertDto paymentinsertDto) {
        Payment paydata=paymentService.addPaymentForMember(paymentinsertDto.getMemberid(), paymentinsertDto.getRegister(), paymentinsertDto.getPay());
        if(paydata != null){
            return ResponseEntity.ok(ResultDto.of("resultcode","급여정보삽입완료",paydata));
        }else{
            return ResponseEntity.ok(ResultDto.of("resultcode","실패",null));
        }
    }
    @PostMapping("/allpayment")
    @ResponseBody
    public ResponseEntity<ResultDto<List<PaymentDto>>> findmember_forPayment(@RequestBody Member member){
        List<PaymentDto> paymentDtos = paymentService.findMemberid_ForAllPayment(member.getMemberid());
        if(paymentDtos!=null){
           return ResponseEntity.ok(ResultDto.of("resultcode","유저에대한 조회 성공",paymentDtos));
        }
        return ResponseEntity.ok(ResultDto.of("resultcode","조회실패",null));
    }

    @PostMapping("/allpayment2")
    @ResponseBody
    public ResponseEntity<ResultDto<Long>> paymentMain(@RequestParam String memberid , @RequestParam long month){
        //   2023-11-20T12:00:00
        // Long month = localdatetomonth(time);
        Long payment = paymentService.paymentMain(month);
        return ResponseEntity.ok(ResultDto.of("resultcode","조회실패",payment));
    }
}




