package com.jobstore.jobstore.dto.request.attendance;

import com.jobstore.jobstore.entity.Attendance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendancefindDto {
    private String memberid;
    private long storeid;
    private long attendid;
    private LocalDateTime start;
    private LocalDateTime end;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime gowork;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime leavework;
    private long wage;
    private String worker;
    private int confirm;
    //private String registertime;
    //private LocalDateTime registertime;



    public Attendance toEntity(){
        return Attendance.builder()
                .start(this.start)
                .end(this.end)
                .attendid(this.attendid)
                .gowork(this.gowork)
                .leavework(this.leavework)
                .wage(this.wage)
                .worker(this.worker)
                .confirm(this.confirm)
                .build();
    }
}
