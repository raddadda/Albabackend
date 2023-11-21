package com.jobstore.jobstore.service;

import com.jobstore.jobstore.dto.PaymentDto;
import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.entity.Payment;
import com.jobstore.jobstore.repository.MemberRepository;
import com.jobstore.jobstore.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private MemberRepository memberRepository;

    public Payment addPaymentForMember(String memberid, long storeid, long pay) {
        Member member = memberRepository.findByMemberidAndStoreid(memberid, storeid);

        if (member != null) {
            Payment newPayment = new Payment();
            newPayment.setPay(pay);
            newPayment.setMember(member);
            System.out.println("newPayment : "+newPayment);
            return paymentRepository.save(newPayment);
        } else {
            return null;
        }
    }
    public List<PaymentDto> findMemberid_ForAllPayment(String memberid){
        List<Payment> memberPaymentData=paymentRepository.findByMemberId(memberid);
        List<PaymentDto> result=new ArrayList<>();
        for(Payment paydata:memberPaymentData){
            PaymentDto p= PaymentDto.builder()
                    .payid(paydata.getPayid())
                    .pay(paydata.getPay())
                    .build();
            result.add(p);
        }
        return result;
    }
}
