package com.jobstore.jobstore.service;

import com.jobstore.jobstore.dto.PaymentAdminDto;
import com.jobstore.jobstore.dto.PaymentDto;
import com.jobstore.jobstore.dto.request.payment.PaymentPagenationDto;
import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.entity.Payment;
import com.jobstore.jobstore.entity.PaymentAdmin;
import com.jobstore.jobstore.repository.MemberRepository;
import com.jobstore.jobstore.repository.PaymentAdminRepository;
import com.jobstore.jobstore.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    //어드민 이번달 총 지출액
//    public Long addPaymentForAdmin(String memberid,long month){
//        PaymentAdmin paymentAdmin=new PaymentAdmin();
//        String Role=memberRepository.findByMemberidToRole(memberid);
//        boolean existAdmin= paymentAdminRepository.existsByMemberidAndMonth(memberid,month);
//        long sum = 0;
//        if(Role.equals("ADMIN")){
//            //        System.out.println("페이먼츠 서비스 입니다");
//            long storeid=memberRepository.findeByMemberidForStoreid(memberid);
////        System.out.println("Stordossdjsj:"+storeid);
//            List<Long> lists=paymentRepository.findByStoreidAllmember(storeid,month);
//
//            for (Long payment : lists) { // 리스트 안의 각 Long 값을 가져와 더합니다.
//                sum += payment;
//            }
//            if(!existAdmin) {
//                paymentAdmin.setMemberid(memberid);
//                paymentAdmin.setStoreid(storeid);
//                paymentAdmin.setMonth(month);
//                paymentAdmin.setSum(sum);
//                paymentAdminRepository.save(paymentAdmin);
//            }
////        System.out.println("123123123123123123123123123123"+sum);
//            return sum;
//        }
//            return sum;
//    }
    public Long getPaymentForAdmin(String memberid, long month) {
        String role = memberRepository.findByMemberidToRole(memberid);
        if (role.equals("ADMIN")) {
            return calculatePayment(memberid, month);
        }
        return 0L;
    }

    private Long calculatePayment(String memberid, long month) {
        long storeid = memberRepository.findeByMemberidForStoreid(memberid);
        List<Long> lists = paymentRepository.findByStoreidAllmember(storeid, month);
        return lists.stream().mapToLong(Long::longValue).sum();
    }
    public Long insertPaymentForAdmin(String memberid, long month) {
        String role = memberRepository.findByMemberidToRole(memberid);
        if (role.equals("ADMIN")) {
            boolean existAdmin = paymentAdminRepository.existsByMemberidAndMonth(memberid, month);
            if (!existAdmin) {
                long storeid = memberRepository.findeByMemberidForStoreid(memberid);
                boolean existmember=paymentRepository.existsByMemberStoreStoreidAndMonth(storeid,month);
                if(existmember) {
                    long sum = calculatePayment(memberid, month);
                    PaymentAdmin paymentAdmin = new PaymentAdmin();
                    paymentAdmin.setMemberid(memberid);
                    paymentAdmin.setStoreid(storeid);
                    paymentAdmin.setMonth(month);
                    paymentAdmin.setSum(sum);

                    paymentAdminRepository.save(paymentAdmin);
                    return sum;
                }else{
                    return null;
                }
            }
        }
        return null;
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
    //어드민 전체 조회

    //전체 유저 조회 및 페이지네이션


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
        List<Long> recent=paymentRepository.findeByMemberidAndMonth(memberid,month);
        List<Long> last=paymentRepository.findeByMemberidAndMonth(memberid,month-1);
        if(recent!=null && last!=null) {
            long recentSum = 0;
            long lastSum = 0;
            for (Long payment : recent) {
                recentSum += payment;
            }
            for (Long lastPayment : last) {
                lastSum += lastPayment;
            }
            return calculatePercentageChange(recentSum,lastSum);
        }
        return 0.0;
    }
    public double last_recentAdminPercentage(String memberid,long month){
        Long recent = paymentAdminRepository.findBymemberidForAdminPay(memberid, month);
        Long last = paymentAdminRepository.findBymemberidForAdminPay(memberid, month - 1);

        // null 체크 후 0으로 초기화
        if (recent == null) {
            recent = 0L;
        }
        if (last == null) {
            last = 0L;
        }
        return calculatePercentageChange(recent, last);
    }
    public double calculatePercentageChange(long recent, long last) {
        // 증감율 계산
        double percentageChange = 0.0;
        if (last != 0) {
            percentageChange = ((double) (recent - last) / last) * 100;
        } else if (recent != 0) {
            percentageChange = 100.0;
        }

        return percentageChange;
    }
    //달마다 리스트 반환(유저측 페이지 네이션)
    public Map<Long,Long> userAllLists(String memberid,long month){
        Map<Long,Long> userlistmap=new HashMap<>();
        for (int i = 0; month - i > 0; i++) {
            long currentMonth = month - i;
            List<Long> payments = paymentRepository.findeByMemberidAndMonth(memberid, currentMonth);
            long totalPayment = 0;
            for (Long payment : payments) {
                totalPayment += payment;
            }
            userlistmap.put(currentMonth, totalPayment);
        }
        return userlistmap ;
    }
    public PaymentPagenationDto findByMemberidUser(String memberid ,Integer page){
        Integer size=5;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "month");
        Page<Payment> paymentPage = paymentRepository.findByMemberid(memberid, pageRequest);

        List<PaymentDto> paymentDtoList = new ArrayList<>();

        for (Payment payment : paymentPage.getContent()) {
            Map<Long, Long> userlistmap = userAllLists(memberid, payment.getMonth());
            PaymentDto paymentDto = new PaymentDto(payment.getPayid(), payment.getPay(), payment.getMonth(),payment.getRegister(),userlistmap);
            paymentDto.setUserListMap(userlistmap);
            paymentDtoList.add(paymentDto);
        }

        PaymentPagenationDto dto = new PaymentPagenationDto(
                paymentDtoList,
                paymentPage.getContent().size(),
                paymentPage.getNumber(),
                paymentPage.getTotalPages(),
                paymentPage.hasNext()
        );
        return dto;
    }

    //어드민 달마다 페이지 네이션
    public PaymentPagenationDto findByMemberidAdmin(String memberid ,Integer page){
        Integer size=5;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "month");
        Page<PaymentAdmin> paymentAdmins=paymentAdminRepository.findBymemberid(memberid,pageRequest);
        Page<PaymentAdminDto> toMap=paymentAdmins.map(m->new PaymentAdminDto(m.getMemberid(),m.getStoreid(),m.getMonth(),m.getSum()));
        PaymentPagenationDto dto=new PaymentPagenationDto(
                toMap.getContent(),
                toMap.getContent().size(),
                toMap.getNumber(),
                toMap.getTotalPages(),
                toMap.hasNext()
        );
        return dto;
    }
    public Map<String,Long> AdminfindEachMemberPay(String memberid,long month){
        long storeid = memberRepository.findeByMemberidForStoreid(memberid);

        // 해당 가게 내의 멤버들의 memberid 목록을 가져옵니다.
        List<Object[]> memberList = paymentRepository.findMonthlyPayByStoreAndMonth(storeid, month);

        // 각 멤버의 월급을 저장할 맵 생성
        Map<String, Long> monthlyPayMap = new HashMap<>();

        // 각 멤버별로 월급을 계산하여 맵에 추가
        for (Object[] memberPay : memberList) {
            String memberId = (String) memberPay[0];
            Long monthlyPay = (Long) memberPay[1];
            monthlyPayMap.put(memberId, monthlyPay);
        }

        return monthlyPayMap;
    }
}
