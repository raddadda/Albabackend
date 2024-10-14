package com.jobstore.jobstore.service;

import com.jobstore.jobstore.dto.request.attendance.AttendanceDto;
import com.jobstore.jobstore.dto.request.attendance.AttendanceUpdateDto;
import com.jobstore.jobstore.dto.request.attendance.AttendancefindDto;
import com.jobstore.jobstore.dto.response.attendance.AttendanceHistoryDto;
import com.jobstore.jobstore.entity.Attendance;
import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.entity.Role;
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
        Member member = memberRepository.findByMemberid2(attendanceUpdateDto.getMemberid());
        if(attendance != null){
            if (member != null) {
                attendanceRepository.delete(attendance);
                return true;
            }
        }
        return false;
    }
    //어드민 승인
    public AttendanceUpdateDto confirmAttendance(AttendanceUpdateDto attendanceUpdateDto,Member member) {
        Attendance attendance = attendanceRepository.findByWorkderAndAttendid(attendanceUpdateDto.getWorker(), attendanceUpdateDto.getAttendid());
        if (attendance != null) {
            if (member != null) {
                if(attendanceUpdateDto.getConfirm() == 1){
                    if(attendance.getLeavework() != null){
                        attendance.setConfirm(attendanceUpdateDto.getConfirm());
                        attendanceRepository.save(attendance);
                        return attendanceUpdateDto;
                    }
                }
                return null;
            }
        }
        return null;
    }
    //어드민 전체worker 조회
    public HashMap workerList(String memberid){
        Member member = memberRepository.findByMemberid2(memberid);
        if(member != null){
            List<Attendance> attendances = attendanceRepository.findByMemberMemberid(memberid);
            HashMap<String,String> workerList = new HashMap<>();
            List<String> result = new ArrayList<>();
            for(Attendance attendList : attendances){
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
    public Attendance findByWorkderAndAttendid(String worker,long attendid){
        return attendanceRepository.findByWorkderAndAttendid(worker,attendid);
    }
    public long payCalculate(AttendanceUpdateDto attendanceUpdateDto,Member member,Attendance attendance){
        long result;
        if(member != null){
            if(attendance != null){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                LocalDateTime leave = attendance.getLeavework();
                String formatLeave = leave.format(formatter);
                LocalDateTime go = attendance.getGowork();
                String formatGo = go.format(formatter);

                Duration duration2 = Duration.between(go, leave); // 두 시간의 차이 계산
                long hours = duration2.toHours(); // 시간 단위로 시간 차이 구하기
                long minutes = duration2.toMinutes(); // 분 단위로 시간 차이 구하기
                result = hours*attendance.getWage();
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
        long result = 0;
        //해당 memberid의 attendance목록 불러오기
        List<Attendance> attendance= attendanceRepository.findByWorker(memberid);
        if(attendance != null){
            for(Attendance attendlist : attendance){
                if(attendlist.getConfirm()==1){
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
        }
        return result;
    }

    //이번주 일한 시간
    public long localDateTimeToWeek(String memberid){
        LocalDateTime now = LocalDateTime.now(); // 현재 날짜와 시간
        LocalDateTime startOfWeek = now.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)).withHour(0).withMinute(0).withSecond(0); // 이번 주 시작일
        LocalDateTime endOfWeek = now.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY)).withHour(23).withMinute(59).withSecond(59); // 이번 주 마지막 일시
        List<Attendance> attendances= attendanceRepository.findByWorker(memberid);
        long result =0;
        for(Attendance attendance : attendances){
            if(attendance.getConfirm()==1){
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

        }
        return result;
    }
    public boolean confirmCheck(String memberid){
        List<Attendance> attendances =attendanceRepository.findByMemberMemberid(memberid);
        for(Attendance list : attendances){
            if(list.getConfirm()==0){
                return false;
            }
        }
        return true;
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

        public Map getUserMonthData(Member member){
        LocalDateTime localDateTime = LocalDateTime.now();

        Map<Long,Long> result = new LinkedHashMap();
        for(int i=0; i<5; i++){
            LocalDateTime dateTime = localDateTime.minusMonths(i);
            long monthValue = dateTime.getMonthValue();
            long yearValue = dateTime.getYear();
            String month = String.valueOf(dateTime.getMonthValue());
            String year = String.valueOf(dateTime.getYear());
            String combinedString = year+month;
            long date = Integer.parseInt(combinedString);
            result.put(date,workMonth(monthValue,member.getMemberid()));

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
        // 페이지 번호 검증 (1부터 시작하는 경우)
        Integer pageNumber = Math.max(0, page - 1);
        Integer size = 5;  // 한 페이지 당 사이즈
        PageRequest pageRequest = PageRequest.of(pageNumber, size, Sort.Direction.DESC, "start");

        // Role이 유효한지 확인
        if (role == null) {
            throw new IllegalArgumentException("Role must not be null");
        }

        Page<Attendance> attendances;
        switch (Role.valueOf(role)) {
            case USER:
                attendances = attendanceRepository.findByWorker(memberid, pageRequest);
                break;
            case ADMIN:
                attendances = attendanceRepository.findByMemberMemberid(memberid, pageRequest);
                break;
            default:
                return null;  // 역할이 유효하지 않으면 null 반환 (또는 예외를 던질 수 있음)
        }
        // DTO로 변환
        Page<AttendancefindDto> toMap = attendances.map(this::mapToAttendancefindDto);

        return new AttendanceHistoryDto(
                toMap.getContent(),
                toMap.getContent().size(),
                toMap.getNumber(),
                toMap.getTotalPages(),
                toMap.hasNext()
        );
    }
    // DTO 변환 메서드
    private AttendancefindDto mapToAttendancefindDto(Attendance m) {
        return new AttendancefindDto(
                m.getMember().getMemberid(),
                m.getStoreid(),
                m.getAttendid(),
                m.getStart(),
                m.getEnd(),
                m.getGowork(),
                m.getLeavework(),
                m.getWage(),
                m.getWorker(),
                m.getConfirm()
        );
    }
}

