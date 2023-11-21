package com.jobstore.jobstore.service;

import com.jobstore.jobstore.dto.AttendanceDto;
import com.jobstore.jobstore.dto.MemberDto;
import com.jobstore.jobstore.entity.Attendance;
import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.entity.Store;
import com.jobstore.jobstore.repository.AttendanceRepository;
import com.jobstore.jobstore.repository.MemberRepository;
import com.jobstore.jobstore.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    public void createAttendance(AttendanceDto attendanceDto){
        Member member = memberRepository.findByMemberid2(attendanceDto.getMemberid());
        if (member != null) {
            Attendance attendance = attendanceDto.toEntity();
            //attendance.setLeavework(attendanceDto.getGowork());
            attendance.setMember(member);
            attendanceRepository.save(attendance);
        }
    }
    public void goworkAttendance(AttendanceDto attendanceDto){
        Member member = memberRepository.findByMemberid2(attendanceDto.getMemberid());
        if (member != null) {
            Attendance attendance = attendanceDto.toEntity();
            attendance.setGowork(attendanceDto.getGowork());
            attendance.setMember(member);
            attendanceRepository.save(attendance);
        }
    }
    public void leaveworkAttendance(AttendanceDto attendanceDto){
        Member member = memberRepository.findByMemberid2(attendanceDto.getMemberid());
        if (member != null) {
            Attendance attendance = attendanceDto.toEntity();
            attendance.setLeavework(attendanceDto.getLeavework());
            attendance.setMember(member);
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
    public void updateAttendance(AttendanceDto attendanceDto){
        Attendance attendance = attendanceRepository.findByAttendid(attendanceDto.getAttendid());
        //Optional<Attendance> optionalAttendance = attendanceRepository.findById(attendanceDto.getAttendid());
        Member member = memberRepository.findByMemberid2(attendanceDto.getMemberid());
        if(attendance != null){
            Attendance existAttendance = attendanceDto.toEntity();
            if (member != null) {
                existAttendance.setEnd(attendance.getEnd());
                existAttendance.setStart(attendance.getStart());
                attendanceRepository.save(existAttendance);
            }
        }
    }

    public void deleteAttendance(AttendanceDto attendanceDto){
        Optional<Attendance> attendance = attendanceRepository.findById(attendanceDto.getAttendid());
        if (attendance.isPresent()) {
            Attendance existAttendance = attendance.get();
            Member member = memberRepository.findByMemberid2(attendanceDto.getMemberid());
            if(member != null){
                attendanceRepository.delete(existAttendance);
//deleteById
            }

        }
    }
    public void monthpay(long result){


        // paymentRepository.save();
    }
    public long payCalculate(AttendanceDto attendanceDto){
        Member member = memberRepository.findByMemberid2(attendanceDto.getMemberid());
        long result;
        if(member != null){
            Attendance attendance = attendanceRepository.findByAttendid(attendanceDto.getAttendid());
            System.out.println("attendance.getLeavework():"+attendance.getLeavework());
            System.out.println("attendance.getGowork():"+attendance.getGowork());
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


            System.out.println("formatLeave:"+formatLeave);
            System.out.println("formatGo"+formatGo);
            Duration duration2 = Duration.between(go, leave); // 두 시간의 차이 계산
            long hours = duration2.toHours(); // 시간 단위로 시간 차이 구하기
            long minutes = duration2.toMinutes(); // 분 단위로 시간 차이 구하기
            System.out.println("hours: "+hours);
            System.out.println("minutes: "+minutes);
            result = minutes*attendanceDto.getWage();



//            minutes %= 60;
////            320 360-20(20분 지각) = 340
////                    360-40(40분 지각) = 320
//            if(minutes  <=30){
//                resultpay-=60;
//            }
            return result;
        }
    return -1;
    }
}
