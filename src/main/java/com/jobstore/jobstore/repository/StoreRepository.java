package com.jobstore.jobstore.repository;

import com.jobstore.jobstore.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store,Long> {
   Store findByCompanynumber(String companynumber);
}
