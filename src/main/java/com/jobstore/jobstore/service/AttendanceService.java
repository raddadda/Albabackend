package com.jobstore.jobstore.service;

import com.jobstore.jobstore.dto.request.attendance.AttendanceDto;
import com.jobstore.jobstore.dto.request.attendance.AttendanceUpdateDto;
import com.jobstore.jobstore.dto.request.attendance.AttendancefindDto;
import com.jobstore.jobstore.dto.response.attendance.AttendanceHistoryDto;
import com.jobstore.jobstore.entity.Attendance;
import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.repository.AttendanceRepository;
import com.jobstore.jobstore.repository.MemberRepository;
import com.jobstore.jobstore.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.swing.text.Style;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;


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

    //생성
    public boolean createAttendance(AttendanceDto attendanceDto){
        Member member = memberRepository.findByMemberid2(attendanceDto.getMemberid());
        if (member != null) {
            long storeid = memberRepository.findeByMemberidForStoreid(member.getMemberid());
            Attendance attendance = attendanceDto.toEntity();
            attendance.setMember(member);
            attendance.setStoreid(storeid);
            attendanceRepository.save(attendance);
            return true;
        }
        return false;
    }
    //전체조회
    public List<Attendance> findAllAttendance(){
        List<Attendance> Result = new ArrayList<>();
        List<Attendance> List = attendanceRepository.findAll();
        for(Attendance list : List){
            Result.add(list);
        }
        return Result;
    }
    //유저 출근
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
    //유저 퇴근
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
    //어드민 수정
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

    //어드민 삭제
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
    //어드민 승인
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
    //어드민 전체worker 조회
    public HashMap workerList(String memberid){
        Member member = memberRepository.findByMemberid2(memberid);
//        System.out.println("-----------------3-----------------");
        if(member != null){
            List<Attendance> attendances = attendanceRepository.findByMemberMemberid(memberid);
//            System.out.println("-----------------4-----------------");
            HashMap<String,String> workerList = new HashMap<>();
            List<String> result = new ArrayList<>();
            for(Attendance attendList : attendances){
//                System.out.println("-----------------5-----------------");
                String key = attendList.getWorker();
                Member worker = memberRepository.findByWorker(attendList.getWorker());
                String value=worker.getName();
                workerList.put(key,value);
            }
            return workerList;
        }
        return null;

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
     * Attendance 유저 데이터 통계
     */

    //이번달 일한 시간
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

    //이번주 일한 시간
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

    //저번달과 이번달 비교 퍼센트
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

//    public List<LocalDateTime> getLocalDateTimesInFiveMonths(LocalDateTime baseDateTime) {
//        List<LocalDateTime> dateTimeList = new ArrayList<>();
//
//        // 입력된 기준 날짜로부터 5달간의 LocalDateTime 값 추가
//        for (int i = 0; i < 5; i++) {
//            LocalDateTime dateTime = baseDateTime.plusMonths(i);
//            dateTimeList.add(dateTime);
//        }
//    }

        public Map getUserMonthData(String memberid){
        LocalDateTime localDateTime = LocalDateTime.now().plusMonths(4);
        //localDateTime.getMonthValue()

        //System.out.println("monthValue"+monthValue);
        Map<Long,Long> result = new LinkedHashMap();
        for(int i=0; i<5; i++){

            LocalDateTime dateTime = localDateTime.minusMonths(i);
            long monthValue = dateTime.getMonthValue();
            long yearValue = dateTime.getYear();
            String month = String.valueOf(dateTime.getMonthValue());
            String year = String.valueOf(dateTime.getYear());
            String combinedString = year+month;
            long date = Integer.parseInt(combinedString);
            result.put(date,workMonth(monthValue,memberid));
//            result.put(monthValue,workMonth(monthValue,memberid));
//            if(monthValue==1){
//                monthValue=12;
//            }else {
//                monthValue--;
//            }

        }
        return result;

    }

    /**
     * Attendance admin 그래프 통계
     */
    public HashSet getUser(String memberid){
        Member member = memberRepository.findByMemberid2(memberid);
        if(member != null){
            List<Attendance> attendance = attendanceRepository.findByMemberMemberid(memberid);
            HashSet<String> work = new HashSet<>();
            for(Attendance attendList : attendance){
                work.add(attendList.getWorker());
            }

            return work;
        }

        return null;
    }

    public double getUserData(String memberid,String worker){
        Member admin = memberRepository.findByMemberid2(memberid);
        Member user = memberRepository.findByMemberid2(worker);
        if(admin != null && user != null){
            double workPercent = workPercent(memberid);
            return workPercent;
        }

        return -1;
    }

    public HashMap<String, Long> getAttendData(String memberid){
        Member member = memberRepository.findByMemberid2(memberid);
        if(member != null){
            List<Attendance> attendances = attendanceRepository.findByMemberMemberid(memberid);
            HashMap<String,Long> workerList = new HashMap<>();
            List<String> result = new ArrayList<>();
            for(Attendance attendList : attendances){
                String key = attendList.getWorker();
                Long value = workerList.getOrDefault(key, 0L);
                if(attendList.getConfirm() ==1){
                    value+=1;
                }
                workerList.put(key,value);
            }
            return workerList;
        }
        return null;
    }

    /**
     * Attendance History 조회
     */
    public AttendanceHistoryDto getAttendancesByMemberId (String role,String memberid,long storeid,Integer page){
        //한페이지당 사이즈
        Integer size = 3;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "attendid");
        Page<Attendance> attendances;
        if(role.equals("USER")){
            attendances =  attendanceRepository.findByWorker(memberid, pageRequest);
        }else if(role.equals("ADMIN")){
            attendances =  attendanceRepository.findByMemberMemberid(memberid, pageRequest);
        }else{
            return null;
        }
        //work를 DTO로 변환
        Page<AttendancefindDto> toMap = attendances.map(m -> new AttendancefindDto(
                m.getMember().getMemberid(),
                m.getStoreid(),m.getAttendid(),
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

}
