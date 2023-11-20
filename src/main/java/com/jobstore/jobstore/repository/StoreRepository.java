package com.jobstore.jobstore.repository;

import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.entity.Store;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store,Long> {

   Optional<Store> findByStoreid(Long storeid);

   Store findByCompanynumber(String companynumber);

   @Transactional // transaction 내부
   @Modifying // 조회 x
   @Query("DELETE FROM Store s WHERE s.storeid = :storeid")
   int deleteByStoreid(@Param("storeid") long storeid);
}
