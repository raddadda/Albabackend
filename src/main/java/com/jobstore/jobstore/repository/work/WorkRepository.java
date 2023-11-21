package com.jobstore.jobstore.repository.work;


import com.jobstore.jobstore.entity.Work;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface WorkRepository extends JpaRepository<Work,Long> {

    Optional<Work> findByWorkid(long workid);
    Page<Work> findByStoreid(long storeid, Pageable pageable);

    @Transactional
    @Modifying // 조회 X
    @Query("DELETE FROM Work w WHERE w.workid = :id")
    int deletebyWorkid(@Param("id") long id);

}
