package com.jobstore.jobstore.repository;

import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.entity.Work;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface WorkRepository extends JpaRepository<Work,Long> {

    Optional<Work> findByWorkid(long workid);

    
    List<Work> findTop10ByStoreidAndOrderByWorkid(@Param("storeid") long storeid, @Param("workid") long workid);

    @Transactional
    @Modifying // 조회 X
    @Query("DELETE FROM Work w WHERE w.workid = :id")
    int deletebyWorkid(@Param("id") long id);

}
