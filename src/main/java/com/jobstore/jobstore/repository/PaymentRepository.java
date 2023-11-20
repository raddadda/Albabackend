package com.jobstore.jobstore.repository;

import com.jobstore.jobstore.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
}
