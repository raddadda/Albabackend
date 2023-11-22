package com.jobstore.jobstore.repository;

import com.jobstore.jobstore.entity.Payment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Payment p WHERE p.member.memberid = :memberId AND p.member.store.storeid = :storeId")
    void deleteByMemberIdAndStoreId(@Param("memberId") String memberId, @Param("storeId") Long storeId);

    @Query("SELECT p FROM Payment p WHERE p.member.memberid = :memberid")
    List<Payment> findByMemberId(@Param("memberid") String memberid);
}
