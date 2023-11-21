package com.jobstore.jobstore.Controller;

import com.jobstore.jobstore.dto.request.PaymentinsertDto;
import com.jobstore.jobstore.dto.response.ResultDto;
import com.jobstore.jobstore.entity.Payment;
import com.jobstore.jobstore.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<ResultDto<Payment>> addPayment(@RequestBody PaymentinsertDto paymentinsertDto) {
        Payment paydata=paymentService.addPaymentForMember(paymentinsertDto.getMemberid(), paymentinsertDto.getStoreid(), paymentinsertDto.getPay());
        if(paydata != null){
            return ResponseEntity.ok(ResultDto.of("resultcode","급여정보 삽입완료",paydata));
        }else{
            return ResponseEntity.ok(ResultDto.of("resultcode","message",null));
        }
    }
// {
//     System.out.println("12312312");
//     if (memberid == null || memberid.isEmpty()) {
//         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Member ID is required");
//     }
//
//     Payment Payment = paymentService.addPaymentForMember(memberid, storeid, pay);
//     if (Payment != null) {
//         return ResponseEntity.ok("Payment added successfully");
//     } else {
//         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
//     }
// }
    }



