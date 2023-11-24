package com.jobstore.jobstore.repository;

import com.jobstore.jobstore.entity.PaymentAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentAdminRepository extends JpaRepository<PaymentAdmin,String> {
}
