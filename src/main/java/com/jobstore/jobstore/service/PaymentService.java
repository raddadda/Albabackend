package com.jobstore.jobstore.service;

import com.jobstore.jobstore.dto.PaymentAdminDto;
import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.entity.Payment;
import com.jobstore.jobstore.entity.PaymentAdmin;
import com.jobstore.jobstore.repository.MemberRepository;
import com.jobstore.jobstore.repository.PaymentAdminRepository;
import com.jobstore.jobstore.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PaymentAdminRepository paymentAdminRepository;

    public Payment addPaymentForMember(String memberid, LocalDateTime register, long pay) {
        // Member member = memberRepository.findByMemberidAndStoreid(memberid, storeid);
        Member member = memberRepository.findByMemberid2(memberid);
        long storeid=memberRepository.findeByMemberidForStoreid(memberid);
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@"+storeid);
        if (member != null) {
            Payment newPayment = new Payment();
            newPayment.setPay(pay);

            newPayment.setMonth(localDateTimeToMonth(register));
            newPayment.setRegister(register);
            newPayment.setMember(member);
            System.out.println("newPayment : "+newPayment);
            return paymentRepository.save(newPayment);
        } else {
            return null;
        }
    }
    public Long addPaymentForAdmin(String memberid,long month){
        PaymentAdmin paymentAdmin=new PaymentAdmin();
        String Role=memberRepository.findByMemberidToRole(memberid);
        long sum = 0;
        if(Role.equals("ADMIN")){
            //        System.out.println("페이먼츠 서비스 입니다");
            long storeid=memberRepository.findeByMemberidForStoreid(memberid);
//        System.out.println("Stordossdjsj:"+storeid);
            List<Long> lists=paymentRepository.findByStoreidAllmember(storeid,month);

            for (Long payment : lists) { // 리스트 안의 각 Long 값을 가져와 더합니다.
                sum += payment;
            }
            paymentAdmin.setMemberid(memberid);
            paymentAdmin.setStoreid(storeid);
            paymentAdmin.setMonth(month);
            paymentAdmin.setSum(sum);
            paymentAdminRepository.save(paymentAdmin);
//        System.out.println("123123123123123123123123123123"+sum);
            return sum;
        }
            return sum;
    }
//    public List<PaymentDto> findMemberid_ForAllPayment(String memberid){
//        List<Payment> memberPaymentData=paymentRepository.findByMemberId(memberid);
//        List<PaymentDto> result=new ArrayList<>();
//        for(Payment paydata:memberPaymentData){
//            PaymentDto p= PaymentDto.builder()
//                    .payid(paydata.getPayid())
//                    .pay(paydata.getPay())
//                    .build();
//            result.add(p);
//        }
//        return result;
//    }
    public List<PaymentAdminDto> findByMemberidAdmin(String memberid){
        List<PaymentAdmin> admindata=paymentAdminRepository.findBymemberid(memberid);
        List<PaymentAdminDto> result=new ArrayList<>();
        for(PaymentAdmin paydata:admindata){
            PaymentAdminDto paymentAdminDto= PaymentAdminDto.builder()
                    .memberid(paydata.getMemberid())
                    .storeid(paydata.getStoreid())
                    .sum(paydata.getSum())
                    .month(paydata.getMonth())
                    .build();
            result.add(paymentAdminDto);
        }
        return result;
    }
    //전체 유저 조회 및 페이지네이션
public List<Payment> findAll(){
    return paymentRepository.findAll();
}


public List<Payment> findByCursor(String memberid,long cursor,int size) {
    return paymentRepository.findByCursor(memberid, cursor, size);
}

    /**
     월급 계산
     */
    public Long paymentMain(long month){
        List<Payment> payments =  paymentRepository.findByMonth(month);
       // Payment payment =  paymentRepository.findByMonth(month);
        long result =0;
        //List<Long> payList = new ArrayList<>();
        for(Payment payment : payments){
            result += payment.getPay();
        }
        return result;
    }
    /**
     주급 계산
     */
    public Long wageWeek(long month){
        List<Payment> payments =  paymentRepository.findByMonth(month);
        // Payment payment =  paymentRepository.findByMonth(month);
        long result =0;
        //List<Long> payList = new ArrayList<>();
        for(Payment payment : payments){
            result += payment.getPay();
        }
        return result;
    }
    public boolean checkLocaltime(LocalDateTime before,LocalDateTime after){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Duration duration = Duration.between(before, after);
        //두 시간차가 음수일때 , 즉 after시간보다 before시간이 더 최근일때를 의미한다.
        if(duration.toMinutes() <0){
            return false;
        }
        return true;

    }
    public long localDateTimeToMonth(LocalDateTime localDateTime){
        return localDateTime.getMonthValue();
    }

    public long localDateTimeToWeek(LocalDateTime time,String memberid){

        LocalDateTime now = LocalDateTime.now(); // 현재 날짜와 시간

        LocalDateTime startOfWeek = now.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)); // 이번 주 시작일
        LocalDateTime endOfWeek = now.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY)).withHour(23).withMinute(59).withSecond(59); // 이번 주 마지막 일시

        long month = localDateTimeToMonth(time);

        List<Payment> payments =  paymentRepository.findByRegister(memberid);
        long result =0;
        //List<Long> payList = new ArrayList<>();
        for(Payment payment : payments){
            if(checkLocaltime(startOfWeek,payment.getRegister())){
                System.out.println("startOfWeek : "+startOfWeek);
                System.out.println("payment.getRegister(): "+payment.getRegister());

                if(checkLocaltime(payment.getRegister(),endOfWeek)){
                    System.out.println("endOfWeek : "+endOfWeek);
                    System.out.println("payment.getRegister(): "+payment.getRegister());
                    result += payment.getPay();

                }
            }
            //result += payment.getPay();
        }
        return result;
        //System.out.println("result : "+result);
        //return result;
       // Payment payments =  paymentRepository.findByRegister(memberid);
        // System.out.println("payments.getRegister: "+payments.getRegister());
        //payments.getRegister()

     //   System.out.println("endOfWeek: "+endOfWeek);


    }
    //저번달 대비 이번달 퍼센테이지 반환 서비스
    public double last_recentpercentage(String memberid,long month){
        long recent=paymentRepository.findeByMemberidAndMonth(memberid,month);
        long last=paymentRepository.findeByMemberidAndMonth(memberid,month-1);
        System.out.println("AAAAAAAAAAAAAA:     "+recent+"BBBBBBBBBBBBBB:   " +last);
        return calculatePercentageChange(recent,last);
    }
    public double calculatePercentageChange(long recent, long last) {
        // 증감율 계산
        double percentageChange = 0.0;
        if (last != 0) {
            percentageChange = ((double) (recent - last) / last) * 100;
        } else if (recent != 0) {
            percentageChange = 100.0;
        }

//        if (percentageChange < 0) {
//            percentageChange = -percentageChange;
//        }

        return percentageChange;
    }
}
