package com.jobstore.jobstore.repository;

import com.jobstore.jobstore.entity.Attendance;
import com.jobstore.jobstore.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    Attendance findByAttendid(Long attendid);
  //  Optional<Member> findByMemberid(String memberid);

}
