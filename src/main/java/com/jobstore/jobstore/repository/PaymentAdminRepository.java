package com.jobstore.jobstore.repository;

import com.jobstore.jobstore.entity.PaymentAdmin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentAdminRepository extends JpaRepository<PaymentAdmin,String> {
    @Query("SELECT p FROM PaymentAdmin p WHERE p.memberid=:memberid")
    Page<PaymentAdmin> findBymemberid(@Param("memberid") String memberid, Pageable pageable);

    @Query("SELECT p.sum FROM PaymentAdmin p WHERE p.memberid=:memberid AND p.month=:month")
    Long findBymemberidForAdminPay(@Param("memberid") String memberid,@Param("month") long month);

    boolean existsByMemberidAndMonth(String memberid,long month);

    @Query("SELECT p FROM PaymentAdmin p WHERE p.memberid=:memberid AND p.month=:month")
    PaymentAdmin findBymemberidAndMonth(@Param("memberid") String memberid, @Param("month") long month);
}
