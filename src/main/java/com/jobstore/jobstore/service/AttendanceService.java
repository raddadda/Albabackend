package com.jobstore.jobstore.service;

import com.jobstore.jobstore.dto.Attendance.*;
import com.jobstore.jobstore.dto.AttendanceHistoryDto;
import com.jobstore.jobstore.dto.WorkDto;
import com.jobstore.jobstore.dto.response.work.WorkPagenationDto;
import com.jobstore.jobstore.entity.Attendance;
import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.entity.Payment;
import com.jobstore.jobstore.entity.Work;
import com.jobstore.jobstore.repository.AttendanceRepository;
import com.jobstore.jobstore.repository.MemberRepository;
import com.jobstore.jobstore.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;


@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentService paymentService;
    public void createAttendance(AttendanceDto attendanceDto){
        Member member = memberRepository.findByMemberid2(attendanceDto.getMemberid());
        if (member != null) {
            long storeid = memberRepository.findeByMemberidForStoreid(member.getMemberid());

           // memberRepository.fimember.getMemberid()
            Attendance attendance = attendanceDto.toEntity();
            attendance.setMember(member);
            attendance.setStoreid(storeid);
            attendanceRepository.save(attendance);
        }
    }
    public void goworkAttendance(AttendanceUpdateDto attendanceUpdateDto){
        Member member = memberRepository.findByMemberid2(attendanceUpdateDto.getMemberid());
        System.out.println("5555555");
        if (member != null) {
            System.out.println("66666666");
            Attendance attendance = attendanceRepository.findByWorkderAndAttendid(attendanceUpdateDto.getWorker(),attendanceUpdateDto.getAttendid());
            System.out.println("77777777");
            //Attendance attendance = attendanceDto.toEntity();
            LocalDateTime Gowork = attendanceUpdateDto.getGowork();
            attendance.setGowork(attendanceUpdateDto.getGowork());
            //attendance.setMember(member);
            attendanceRepository.save(attendance);
        }
    }
    public void leaveworkAttendance(AttendanceUpdateDto attendanceUpdateDto){
        Member member = memberRepository.findByMemberid2(attendanceUpdateDto.getMemberid());
        if (member != null) {
            Attendance attendance = attendanceRepository.findByWorkderAndAttendid(attendanceUpdateDto.getWorker(),attendanceUpdateDto.getAttendid());
            attendance.setLeavework(attendanceUpdateDto.getLeavework());
           // attendance.setMember(member);
            attendanceRepository.save(attendance);
        }
    }
    @Transactional
    public List<Attendance> findAllAttendance(){
        List<Attendance> Result = new ArrayList<>();
        List<Attendance> List = attendanceRepository.findAll();
        for(Attendance list : List){
            //해당 회원의 장바구니를 찾아서 장바구니 아이템을 반환시킨다.
            //if(basketItem.getBasket().getId() == memberBasketId){
            Result.add(list);
           //}
        }
        return Result;
       // Optional<Member> member = memberRepository.findByMemberid(attendanceDto.getMemberid());
//        System.out.println("findAllAttendance");
//        List<Attendance> result = attendanceRepository.findAll();
//        List<AttendanceDto> list =new ArrayList<AttendanceDto>();
//        for(Attendance attendanceList : result){
//            AttendanceDto data = new AttendanceDto();
//            data.set
//            attendanceDtos.add(attendanceList.getEnd());
//
//        }
//
//        System.out.println("findAllAttendance22"+attendance);
//        return attendance;
    }
    public void updateAttendance(AttendanceUpdateDto attendanceUpdateDto){
        System.out.println("attendanceUpdateDto.getAttendid(): "+attendanceUpdateDto.getAttendid());
        Attendance attendance = attendanceRepository.findByAttendid(attendanceUpdateDto.getAttendid());
        //Attendance attendance = attendanceRepository.findByWorkder(attendanceDto.getWorker());
        //Optional<Attendance> optionalAttendance = attendanceRepository.findById(attendanceDto.getAttendid());
        Member member = memberRepository.findByMemberid2(attendanceUpdateDto.getMemberid());
        if(attendance != null){
           // Attendance existAttendance = attendanceDto.toEntity();
            if (member != null) {
                attendance.setEnd(attendanceUpdateDto.getEnd());
                attendance.setStart(attendanceUpdateDto.getStart());
                attendance.setWage(attendanceUpdateDto.getWage());
                attendance.setWorker(attendanceUpdateDto.getWorker());
                attendanceRepository.save(attendance);
            }
        }
    }
    public void deleteAttendance(AttendanceUpdateDto attendanceUpdateDto){
        Attendance attendance = attendanceRepository.findByWorkderAndAttendid(attendanceUpdateDto.getWorker(),attendanceUpdateDto.getAttendid());
        //Optional<Attendance> optionalAttendance = attendanceRepository.findById(attendanceDto.getAttendid());
        Member member = memberRepository.findByMemberid2(attendanceUpdateDto.getMemberid());
        if(attendance != null){
            // Attendance existAttendance = attendanceDto.toEntity();
            if (member != null) {
                attendanceRepository.delete(attendance);
            }
        }
    }
    public void monthpay(long result){


        // paymentRepository.save();
    }
    public long payCalculate(AttendanceUpdateDto attendanceUpdateDto){
        Member member = memberRepository.findByMemberid2(attendanceUpdateDto.getMemberid());
        long result;
        if(member != null){
            Attendance attendance = attendanceRepository.findByWorkderAndAttendid(attendanceUpdateDto.getWorker(),attendanceUpdateDto.getAttendid());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

//            LocalDateTime start = attendance.getStart();
//            String formatStart = start.format(formatter);
//            LocalDateTime end = attendance.getEnd();
//            String formatEnd = end.format(formatter);
//            Duration duration = Duration.between(start, end);
            //formatStart.

            LocalDateTime leave = attendance.getLeavework();
            String formatLeave = leave.format(formatter);
            LocalDateTime go = attendance.getGowork();
            String formatGo = go.format(formatter);

            Duration duration2 = Duration.between(go, leave); // 두 시간의 차이 계산
            long hours = duration2.toHours(); // 시간 단위로 시간 차이 구하기
            long minutes = duration2.toMinutes(); // 분 단위로 시간 차이 구하기

            result = minutes*attendanceUpdateDto.getWage();

            return result;
        }
    return -1;
    }

    public boolean existGoworkCheck(AttendanceUpdateDto attendanceUpdateDto){
        Attendance attendance = attendanceRepository.findByWorkderAndAttendid(attendanceUpdateDto.getWorker(),attendanceUpdateDto.getAttendid());
        if(attendance.getGowork() != null){
            return true;
        }
       return false;
    }

    public Long workMonth(long month,String memberid){
        Member member = memberRepository.findByMemberid2(memberid);
        long result = 0;
        if(member != null){
            //해당 memberid의 attendance목록 불러오기
            List<Attendance> attendance= attendanceRepository.findByWorker(member.getMemberid());
            if(attendance != null){
                for(Attendance attendlist : attendance){
                    long time = attendlist.getLeavework().getMonthValue();

                    if(time == month){
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                        LocalDateTime leave = attendlist.getLeavework();
                        String formatLeave = leave.format(formatter);
                        LocalDateTime go = attendlist.getGowork();
                        String formatGo = go.format(formatter);

                        Duration duration = Duration.between(go, leave); // 두 시간의 차이 계산
                        long minutes = duration.toMinutes(); // 분 단위로 시간 차이 구하기
                        result += minutes;
                    }
                }
            }
            return result;
        }
        return (long)-1;
    }
    //유저: 히스토리x, 퍼센트, 주급,일급
    //어드민: 히스토리, x , 주급,일급
    public double workPercent(String memberid){

        LocalDateTime time = LocalDateTime.now();

        long thisMonth = localDateTimeToMonth(time);
        if(thisMonth == 1){
            long lastMonth = 12;
        }
        long lastMonth = thisMonth-1;
        System.out.println("thisMonth: "+thisMonth);
        System.out.println("lastMonth: "+lastMonth);
        long now= workMonth(thisMonth,memberid);

        long last= workMonth(lastMonth,memberid);

        System.out.println("now: "+now);
        System.out.println("last: "+last);
        if(last != -1){
            double percent = calculatePercentageChange(now,last);

            return percent;
        }
        return -1;
    }

//    public Long workWeek(LocalDateTime time,String memberid){
//        Member member = memberRepository.findByMemberid2(memberid);
//        long result = 0;
//        if(member != null){
//            //해당 memberid의 attendance목록 불러오기
//            List<Attendance> attendance= attendanceRepository.findByWorker(member.getMemberid());
//            if(attendance != null){
//                for(Attendance attendlist : attendance){
//                    attendlist
//                    List<Attendance> attendances =  paymentRepository.findByRegister(memberid);
//
//                    if(time == week){
//                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//                        LocalDateTime leave = attendlist.getLeavework();
//                        String formatLeave = leave.format(formatter);
//                        LocalDateTime go = attendlist.getGowork();
//                        String formatGo = go.format(formatter);
//
//                        Duration duration = Duration.between(go, leave); // 두 시간의 차이 계산
//                        long minutes = duration.toMinutes(); // 분 단위로 시간 차이 구하기
//                        return minutes;
//                    }
//                }
//            }
//        }
//        return (long)-1;
//    }
    public long localDateTimeToMonth(LocalDateTime localDateTime){
        return localDateTime.getMonthValue();
    }

    public long localDateTimeToWeek(String memberid){
        System.out.println("1");
        LocalDateTime now = LocalDateTime.now(); // 현재 날짜와 시간
        System.out.println("2");
        LocalDateTime startOfWeek = now.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)); // 이번 주 시작일
        LocalDateTime endOfWeek = now.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY)).withHour(23).withMinute(59).withSecond(59); // 이번 주 마지막 일시
        System.out.println("3");
        //long month = localDateTimeToMonth(time);
        List<Attendance> attendances= attendanceRepository.findByWorker(memberid);
       // List<Attendance> attendances =  attendanceRepository.findByLeavework(memberid);
        System.out.println("4");
        long result =0;
        //List<Long> payList = new ArrayList<>();
        for(Attendance attendance : attendances){
           // attendance.
            System.out.println("5");
            if(paymentService.checkLocaltime(startOfWeek,attendance.getLeavework())){
                System.out.println("startOfWeek : "+startOfWeek);
                System.out.println("payment.getRegister(): "+attendance.getLeavework());

                if(paymentService.checkLocaltime(attendance.getLeavework(),endOfWeek)){
                    System.out.println("endOfWeek : "+endOfWeek);
                    System.out.println("payment.getRegister(): "+attendance.getLeavework());
                    //result += attendance.getPay();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                        LocalDateTime leave = attendance.getLeavework();
                        String formatLeave = leave.format(formatter);
                        LocalDateTime go = attendance.getGowork();
                        String formatGo = go.format(formatter);

                        Duration duration = Duration.between(go, leave); // 두 시간의 차이 계산
                        result += duration.toMinutes(); // 분 단위로 시간 차이 구하기

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
//    public Slice<Attendance> getAttendancesById(Long id, Pageable pageable) {
//        return attendanceRepository.findByAttendidGreaterThanOrderByAttendid(id, pageable);
//    }
//    public int findAllSize(){
//        return attendanceRepository.findAll().size();
//        //return attendanceRepository.findByFirstpage(size);
//    }
//    public List<Attendance> findByCursor(String memberid,long cursor,int size){
//        return attendanceRepository.findByCursor(memberid,cursor,size);
//    }

//    public Page<Attendance> getAttendancesByMemberId(long storeid, int page, int size) {
//       // PageRequest pageRequest = PageRequest.of(page, size);
//
//        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "attendid"));
//
//        return attendanceRepository.findAllByStoreid(storeid, pageRequest);
//    }

    public AttendanceHistoryDto getAttendancesByMemberId (String role,String memberid,long storeid,Integer page){
       // memberid,storeid,cursor,size
        Integer size = 3;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "attendid");
        //long storeid = memberRepository.findeByMemberidForStoreid(memberid);
        // attendanceRepository.findBy
        if(role.equals("USER")){
            Page<Attendance> attendances =  attendanceRepository.findByWorker(memberid, pageRequest);
            System.out.println("attendances"+attendances.isEmpty());
            //work를 DTO로 변환
            Page<AttendanceDto> toMap = attendances.map(m -> new AttendanceDto(
                    m.getMember().getMemberid(),
                    m.getStoreid(),
                    m.getStart(), m.getEnd(),
                    m.getGowork(),m.getLeavework(),
                    m.getWage(),m.getWorker()
            ));

            AttendanceHistoryDto dto = new AttendanceHistoryDto(
                    toMap.getContent(),
                    toMap.getContent().size(),
                    toMap.getNumber(),
                    toMap.getTotalPages(),
                    toMap.hasNext()
            );
            return dto;
        }else if(role.equals("ADMIN")){
            Page<Attendance> attendances =  attendanceRepository.findByMemberMemberid(memberid, pageRequest);

            System.out.println("attendances"+attendances.isEmpty());
            //work를 DTO로 변환
            Page<AttendanceDto> toMap = attendances.map(m -> new AttendanceDto(
                    m.getMember().getMemberid(),
                    m.getStoreid(),
                    m.getStart(), m.getEnd(),
                    m.getGowork(),m.getLeavework(),
                    m.getWage(),m.getWorker()
            ));

            AttendanceHistoryDto dto = new AttendanceHistoryDto(
                    toMap.getContent(),
                    toMap.getContent().size(),
                    toMap.getNumber(),
                    toMap.getTotalPages(),
                    toMap.hasNext()
            );
            return dto;
        }

        return null;

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
