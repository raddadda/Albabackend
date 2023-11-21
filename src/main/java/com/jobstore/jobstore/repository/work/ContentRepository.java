package com.jobstore.jobstore.repository.work;

import com.jobstore.jobstore.entity.Contents;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface ContentRepository extends JpaRepository <Contents,Long>  {


    Optional<Contents> findByContentsid (long contentsid);

    @Transactional
    @Modifying // 조회 X
    @Query("DELETE FROM Contents c WHERE c.contentsid = :id")
    int deletebyContentsid(@Param("id") long id);


}
