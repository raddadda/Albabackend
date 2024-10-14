package com.jobstore.jobstore.repository;

import com.jobstore.jobstore.entity.Attendance;
import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.entity.Payment;
import com.jobstore.jobstore.entity.Work;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    Attendance findByAttendid(Long attendid);

    @Query("SELECT a.worker FROM Attendance a WHERE a.member.memberid =:memberid")
    List<String> findByMemberidToWorker(@Param("memberid") String memberid);

    @Query("SELECT a FROM Attendance a WHERE a.member.memberid =:memberid")
    List<String> findByMemberid(@Param("memberid") String memberid);

    @Query("SELECT a FROM Attendance a WHERE a.worker = :worker AND a.attendid = :attendid")
    Attendance findByWorkderAndAttendid(@Param("worker") String worker,@Param("attendid") long attendid);

    @Query(value = "SELECT * FROM attendance LIMIT :size desc", nativeQuery = true)
    List<Attendance> findByFirstpage(@Param("size") int size);

    //findAllAttendance
    @Query(value = "SELECT * FROM attendance WHERE memberid = :memberid AND attendid > :cursor ORDER BY attendid LIMIT :size", nativeQuery = true)
    List<Attendance> findByCursor(@Param("memberid") String memberid,@Param("cursor") Long cursor, @Param("size") int size);

    Page<Attendance> findByMemberMemberid(String memberid, Pageable pageable);

    List<Attendance> findByMemberMemberid(String memberid);

    Page<Attendance> findByWorker(String memberid, Pageable pageable);
    @Query("SELECT a FROM Attendance a WHERE a.worker = :worker")
    List<Attendance> findByWorker(@Param("worker") String worker);
    @Query("SELECT a FROM Attendance a WHERE a.leavework = :leavework")
    List<Attendance> findByMemberMemberid(@Param("leavework") LocalDateTime leavework);

    @Query("SELECT a FROM Attendance a WHERE a.worker = :worker AND a.attendid = :attendid")
    Attendance findAttendancehistory(@Param("worker") String worker,@Param("attendid") long attendid);

    Slice<Attendance> findByAttendidGreaterThanOrderByAttendid(Long attendid, Pageable pageable);

    List<Attendance> findByWorkerAndStartBetween(String workerId, LocalDateTime start, LocalDateTime end);
}

