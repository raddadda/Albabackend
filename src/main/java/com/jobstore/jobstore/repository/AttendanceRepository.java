package com.jobstore.jobstore.repository;

import com.jobstore.jobstore.entity.Attendance;
import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    Attendance findByAttendid(Long attendid);
  //  Optional<Member> findByMemberid(String memberid);

    @Query("SELECT a FROM Attendance a WHERE a.worker = :worker AND a.attendid = :attendid")
    Attendance findByWorkderAndAttendid(@Param("worker") String worker,@Param("attendid") long attendid);

//    @Query(value = "SELECT * FROM attendance LIMIT :size desc", nativeQuery = true)
//    List<Attendance> findByFirstpage(@Param("size") int size);
//
//    //findAllAttendance
//    @Query(value = "SELECT * FROM attendance WHERE memberid > :cursor ORDER BY memberid LIMIT :size", nativeQuery = true)
//    List<Attendance> findByCursor(@Param("memberid") String memberid,@Param("cursor") Long cursor, @Param("size") int size);

    @Query("SELECT a FROM Attendance a WHERE a.worker = :worker AND a.attendid = :attendid")
    Attendance findAttendancehistory(@Param("worker") String worker,@Param("attendid") long attendid);
    //디폴트
}
