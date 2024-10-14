package com.jobstore.jobstore.service;

import com.jobstore.jobstore.dto.request.attendance.AttendanceDto;
import com.jobstore.jobstore.entity.Attendance;
import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.repository.AttendanceRepository;
import com.jobstore.jobstore.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private PaymentService paymentService;

    /**
        이번달 스케줄 불러오기
     */
    public List thisMonthSchedule(LocalDateTime time, String memberid, String role){
        Member member = memberRepository.findByMemberid2(memberid);
        if (member != null) {
            List<Attendance> attendance = new ArrayList<>();
            if(role.equals("USER")){
                attendance = attendanceRepository.findByWorker(member.getMemberid());
            }
            if(role.equals("ADMIN")){
                attendance =  attendanceRepository.findByMemberMemberid(member.getMemberid());
            }

            List<Attendance> result = new ArrayList<>();

            LocalDateTime startOfLastMonth = time.minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime endOfLastMonth = time.withDayOfMonth(1).minusDays(1).withHour(23).withMinute(59).withSecond(59);

            LocalDateTime startOfNextMonth = time.plusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime endOfNextMonth = time.plusMonths(1).withDayOfMonth(1).plusMonths(1).minusDays(1).withHour(23).withMinute(59).withSecond(59);

            for (Attendance attendlist : attendance) {
                LocalDateTime startTime = attendlist.getStart(); // 출근 정보의 시작 시간

                // 출근 정보의 시작 시간이 이전 달, 현재 달, 다음 달의 범위 내에 있는지 확인
                if ((startTime.isAfter(startOfLastMonth) && startTime.isBefore(endOfLastMonth))
                        || (startTime.isAfter(time.withDayOfMonth(1)) && startTime.isBefore(time.plusMonths(1).withDayOfMonth(1)))
                        || (startTime.isAfter(startOfNextMonth) && startTime.isBefore(endOfNextMonth))) {
                    // 원하는 조건을 충족하는 경우 결과 리스트에 추가
                    result.add(attendlist);
                }
            }

            return result;


        }

        return null;
    }


    public List<Attendance> userFindAllSchedule(LocalDateTime time, String memberid, String role) {
        Member member = memberRepository.findByMemberid2(memberid);

        if (member != null) {
            // 시작과 끝 시간 계산
            LocalDateTime startOfLastMonth = time.minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime endOfNextMonth = time.plusMonths(2).withDayOfMonth(1).minusDays(1).withHour(23).withMinute(59).withSecond(59);

            // 데이터베이스에서 이전 달 ~ 다음 달까지의 데이터를 조회
            List<Attendance> attendance = attendanceRepository.findByWorkerAndStartBetween(
                    member.getMemberid(), startOfLastMonth, endOfNextMonth
            );

            // 조건에 맞는 데이터를 결과로 필터링 (필요할 경우)
            List<Attendance> result = new ArrayList<>();
            for (Attendance attendlist : attendance) {
                LocalDateTime startTime = attendlist.getStart(); // 출근 정보의 시작 시간

                // 이전 달, 현재 달, 다음 달에 해당하는지 확인
                if (isWithinRange(startTime, startOfLastMonth, endOfNextMonth)) {
                    result.add(attendlist);
                }
            }

            return result; // 최종 결과 반환
        }

        throw new MemberNotFoundException("Member not found for ID: " + memberid); // null 대신 예외 처리
    }

    private boolean isWithinRange(LocalDateTime startTime, LocalDateTime start, LocalDateTime end) {
        // 범위 내에 있는지 확인하는 유틸리티 함수
        return (startTime.isAfter(start) || startTime.isEqual(start)) &&
                (startTime.isBefore(end) || startTime.isEqual(end));
    }
}

