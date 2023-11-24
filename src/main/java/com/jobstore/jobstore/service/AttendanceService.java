package com.jobstore.jobstore.service;

import com.jobstore.jobstore.dto.Attendance.*;
import com.jobstore.jobstore.entity.Attendance;
import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.repository.AttendanceRepository;
import com.jobstore.jobstore.repository.MemberRepository;
import com.jobstore.jobstore.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public void createAttendance(AttendanceDto attendanceDto){
        Member member = memberRepository.findByMemberid2(attendanceDto.getMemberid());
        if (member != null) {
            Attendance attendance = attendanceDto.toEntity();
            attendance.setMember(member);
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

//    public List<Attendance> findAll(int size){
//        return attendanceRepository.findByFirstpage(size);
//    }
//    public List<Attendance> findByCursor(String memberid,long cursor,int size){
//        return attendanceRepository.findByCursor(memberid,cursor,size);
//    }
}
