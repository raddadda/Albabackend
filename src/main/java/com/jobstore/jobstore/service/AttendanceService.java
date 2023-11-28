package com.jobstore.jobstore.service;

import com.jobstore.jobstore.dto.request.attendance.AttendanceDto;
import com.jobstore.jobstore.dto.request.attendance.AttendanceUpdateDto;
import com.jobstore.jobstore.dto.response.attendance.AttendanceHistoryDto;
import com.jobstore.jobstore.entity.Attendance;
import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.repository.AttendanceRepository;
import com.jobstore.jobstore.repository.MemberRepository;
import com.jobstore.jobstore.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

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

    /**
     * Attendance CRUD
     */
    public boolean createAttendance(AttendanceDto attendanceDto){
        Member member = memberRepository.findByMemberid2(attendanceDto.getMemberid());
        if (member != null) {
            long storeid = memberRepository.findeByMemberidForStoreid(member.getMemberid());
            Attendance attendance = attendanceDto.toEntity();
            attendance.setMember(member);
            attendance.setStoreid(storeid);
            System.out.println("confirm:"+attendance.getConfirm());
            System.out.println("confirm:"+attendance.getEnd());
            attendanceRepository.save(attendance);

            return true;
        }
        return false;
    }
    public boolean goworkAttendance(AttendanceUpdateDto attendanceUpdateDto){
        Member member = memberRepository.findByMemberid2(attendanceUpdateDto.getMemberid());
        if (member != null) {
            Attendance attendance = attendanceRepository.findByWorkderAndAttendid(attendanceUpdateDto.getWorker(),attendanceUpdateDto.getAttendid());
            LocalDateTime Gowork = attendanceUpdateDto.getGowork();
            attendance.setGowork(attendanceUpdateDto.getGowork());
            attendanceRepository.save(attendance);
            return true;
        }
        return false;
    }
    public boolean leaveworkAttendance(AttendanceUpdateDto attendanceUpdateDto){
        Member member = memberRepository.findByMemberid2(attendanceUpdateDto.getMemberid());
        if (member != null) {
            Attendance attendance = attendanceRepository.findByWorkderAndAttendid(attendanceUpdateDto.getWorker(),attendanceUpdateDto.getAttendid());
            attendance.setLeavework(attendanceUpdateDto.getLeavework());
            attendanceRepository.save(attendance);
            return true;
        }
        return false;
    }

    public List<Attendance> findAllAttendance(){
        List<Attendance> Result = new ArrayList<>();
        List<Attendance> List = attendanceRepository.findAll();
        for(Attendance list : List){
            Result.add(list);
        }
        return Result;
    }
    public boolean updateAttendance(AttendanceUpdateDto attendanceUpdateDto){
        Attendance attendance = attendanceRepository.findByAttendid(attendanceUpdateDto.getAttendid());
        Member member = memberRepository.findByMemberid2(attendanceUpdateDto.getMemberid());
        if(attendance != null){
            if (member != null) {
                attendance.setEnd(attendanceUpdateDto.getEnd());
                attendance.setStart(attendanceUpdateDto.getStart());
                attendance.setWage(attendanceUpdateDto.getWage());
                attendance.setWorker(attendanceUpdateDto.getWorker());
                attendanceRepository.save(attendance);
                return true;
            }
        }
        return false;
    }

    public boolean confirmAttendance(AttendanceUpdateDto attendanceUpdateDto) {
        Attendance attendance = attendanceRepository.findByWorkderAndAttendid(attendanceUpdateDto.getWorker(), attendanceUpdateDto.getAttendid());
        Member member = memberRepository.findByMemberid2(attendanceUpdateDto.getMemberid());
        if (attendance != null) {
            if (member != null) {
                if(attendanceUpdateDto.getConfirm() == 1){
                    attendance.setConfirm(attendanceUpdateDto.getConfirm());
                    attendanceRepository.save(attendance);
                }
                return true;
            }
        }
        return false;
    }
    public boolean deleteAttendance(AttendanceUpdateDto attendanceUpdateDto){
        Attendance attendance = attendanceRepository.findByWorkderAndAttendid(attendanceUpdateDto.getWorker(),attendanceUpdateDto.getAttendid());
        //Optional<Attendance> optionalAttendance = attendanceRepository.findById(attendanceDto.getAttendid());
        Member member = memberRepository.findByMemberid2(attendanceUpdateDto.getMemberid());
        if(attendance != null){
            // Attendance existAttendance = attendanceDto.toEntity();
            if (member != null) {
                attendanceRepository.delete(attendance);
                return true;
            }
        }
        return false;
    }
    /**
     * Attendance 급여계산
     */
    public long payCalculate(AttendanceUpdateDto attendanceUpdateDto){
        Member member = memberRepository.findByMemberid2(attendanceUpdateDto.getMemberid());
        long result;
        if(member != null){
            Attendance attendance = attendanceRepository.findByWorkderAndAttendid(attendanceUpdateDto.getWorker(),attendanceUpdateDto.getAttendid());
            if(attendance != null){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                LocalDateTime leave = attendance.getLeavework();
                String formatLeave = leave.format(formatter);
                LocalDateTime go = attendance.getGowork();
                String formatGo = go.format(formatter);

                Duration duration2 = Duration.between(go, leave); // 두 시간의 차이 계산
                long hours = duration2.toHours(); // 시간 단위로 시간 차이 구하기
                long minutes = duration2.toMinutes(); // 분 단위로 시간 차이 구하기
                System.out.println("minutes"+minutes);
                System.out.println("hours"+hours);
                System.out.println("attendanceUpdateDto.getWage()"+attendanceUpdateDto.getWage());
                result = hours*attendanceUpdateDto.getWage();

                return result;
            }
        }
    return -1;
    }

    public boolean existGoworkCheck(AttendanceUpdateDto attendanceUpdateDto){
        Attendance attendance = attendanceRepository.findByWorkderAndAttendid(attendanceUpdateDto.getWorker(),attendanceUpdateDto.getAttendid());
        //이미 출근했을경우
        if(attendance.getGowork() != null){
            return true;
        }
        //아직 출근하지 않았을 경우
       return false;
    }
    /**
     * Attendance 데이터 통계
     */
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
    public double workPercent(String memberid){

        LocalDateTime time = LocalDateTime.now();
        long thisMonth = localDateTimeToMonth(time);
        if(thisMonth == 1){
            long lastMonth = 12;
        }
        long lastMonth = thisMonth-1;
        long now= workMonth(thisMonth,memberid);
        long last= workMonth(lastMonth,memberid);

        if(last != -1){
            double percent = calculatePercentageChange(now,last);

            return percent;
        }
        return -1;
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

    public long localDateTimeToMonth(LocalDateTime localDateTime){
        return localDateTime.getMonthValue();
    }

    public long localDateTimeToWeek(String memberid){
        LocalDateTime now = LocalDateTime.now(); // 현재 날짜와 시간
        LocalDateTime startOfWeek = now.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)); // 이번 주 시작일
        LocalDateTime endOfWeek = now.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY)).withHour(23).withMinute(59).withSecond(59); // 이번 주 마지막 일시
        List<Attendance> attendances= attendanceRepository.findByWorker(memberid);
        long result =0;
        for(Attendance attendance : attendances){
            if(paymentService.checkLocaltime(startOfWeek,attendance.getLeavework())){
                if(paymentService.checkLocaltime(attendance.getLeavework(),endOfWeek)){
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                        LocalDateTime leave = attendance.getLeavework();
                        String formatLeave = leave.format(formatter);
                        LocalDateTime go = attendance.getGowork();
                        String formatGo = go.format(formatter);

                        Duration duration = Duration.between(go, leave); // 두 시간의 차이 계산
                        result += duration.toMinutes(); // 분 단위로 시간 차이 구하기
                }
            }
        }
        return result;
    }
    /**
     * Attendance History 조회
     */
    public AttendanceHistoryDto getAttendancesByMemberId (String role,String memberid,long storeid,Integer page){
        //한페이지당 사이즈
        Integer size = 3;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "attendid");
        if(role.equals("USER")){
            Page<Attendance> attendances =  attendanceRepository.findByWorker(memberid, pageRequest);
            System.out.println("attendances"+attendances.isEmpty());
            //work를 DTO로 변환
            Page<AttendanceDto> toMap = attendances.map(m -> new AttendanceDto(
                    m.getMember().getMemberid(),
                    m.getStoreid(),
                    m.getStart(), m.getEnd(),
                    m.getGowork(),m.getLeavework(),
                    m.getWage(),m.getWorker(),
                    m.getConfirm()
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
                    m.getWage(),m.getWorker(),
                    m.getConfirm()
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

}
