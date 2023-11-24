package com.jobstore.jobstore.controller;

import com.jobstore.jobstore.config.PrincipalDetails;
import com.jobstore.jobstore.dto.PaymentAdminDto;
import com.jobstore.jobstore.dto.request.payment.*;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
@PostMapping("/admin/findall")
@Operation(summary = "Payment api", description = "admin전체 조회 api")
@ResponseBody
public ResponseEntity<ResultDto<List<PaymentAdminDto>>> findAdminAllpayment(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                content = @Content(schema=@Schema(implementation = PaymentAdminFindAllDto.class)))@RequestBody PaymentAdminFindAllDto paymentAdminFindAllDto){
    return ResponseEntity.ok(ResultDto.of("resultcode","전체조회 완료",paymentService.findByMemberidAdmin(paymentAdminFindAllDto.getMemberid())));
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
        return ResponseEntity.ok(ResultDto.of("resultcode","조회성공",new PaymentMainDto(payment,week)));
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
        return ResponseEntity.ok(ResultDto.of("resultcode","성공",paymentService.last_recentpercentage(paymentPercentageDto.getMemberid(),paymentPercentageDto.getMonth())));

    }

    @GetMapping("/allpayment/{memberid}/{cursor}")
    public  ResponseEntity<ResultDto<Object>> getUserById(@PathVariable String memberid
            ,@PathVariable(required = false) Long cursor
    ) {
        //maxcursor와 조회 리스트를 담은 Dto 객체 생성
        PaymentHistoryDto paymentHistoryDto = new PaymentHistoryDto();
        //한페이지당 보여줄 리스트 수
        int size = 3;
        //페이지 커서의 최대 개수
        int maxcursor;
        cursor= cursor*3;
        int max = paymentService.findAll().size();
        if(max%size>0){
            maxcursor = max/size +1;
        }else {
            maxcursor = max/size;
        }
        List<Payment> list =  paymentService.findByCursor(memberid,cursor,size);

        paymentHistoryDto.setPayments(list);
        paymentHistoryDto.setMaxcursor(maxcursor);
        return ResponseEntity.ok(ResultDto.of("성공","두번째부터",paymentHistoryDto));
    }
    @PostMapping("/admin/allpayment")
    @ResponseBody
    public ResponseEntity<ResultDto<Long>> adminAllPay(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = PaymentAdminAllpayment.class)))@RequestBody PaymentAdminAllpayment paymentAdminAllpayment){
//        System.out.println("asdsadasddssadsa:sadasdas"+memberid);
        return ResponseEntity.ok(ResultDto.of("resultcode","월중전체 지출액 성공",paymentService.addPaymentForAdmin(paymentAdminAllpayment.getMemberid(),paymentAdminAllpayment.getMonth())));
    }
}




