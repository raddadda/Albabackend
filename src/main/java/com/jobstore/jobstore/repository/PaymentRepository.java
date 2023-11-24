package com.jobstore.jobstore.repository;

import com.jobstore.jobstore.entity.Payment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Payment p WHERE p.member.memberid = :memberId AND p.member.store.storeid = :storeId")
    void deleteByMemberIdAndStoreId(@Param("memberId") String memberId, @Param("storeId") Long storeId);

    @Query("SELECT p FROM Payment p WHERE p.member.memberid = :memberid")
    List<Payment> findByMemberId(@Param("memberid") String memberid);

    @Query("SELECT p FROM Payment p WHERE p.month = :month")
    List<Payment> findByMonth(@Param("month") Long month);
//    @Query("SELECT p FROM Payment p WHERE p.month = :month")
//    Payment findByMonth(@Param("month") Long month);

    @Query("SELECT pay From Payment p WHERE p.member.store.storeid=:storeid AND p.month=:month")
    List<Long> findByStoreidAllmember(@Param("storeid") long storeid,@Param("month") long month);
    @Query("SELECT p FROM Payment p WHERE p.member.memberid = :memberid")
    List<Payment> findByRegister(@Param("memberid") String memberid);

    @Query("SELECT p.pay from Payment p WHERE p.member.memberid=:memberid AND p.month=:month")
    long findeByMemberidAndMonth(@Param("memberid") String memberid,@Param("month") long month);

    @Query(value = "SELECT * FROM payment WHERE memberid = :memberid AND payid > :cursor ORDER BY payid LIMIT :size", nativeQuery = true)
    List<Payment> findByCursor(@Param("memberid") String memberid,@Param("cursor") Long cursor, @Param("size") int size);
}
