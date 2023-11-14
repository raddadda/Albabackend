package com.jobstore.jobstore.repository;

import com.jobstore.jobstore.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin,String> {
    Optional<Admin> findByAdminid(String adminid);
}
