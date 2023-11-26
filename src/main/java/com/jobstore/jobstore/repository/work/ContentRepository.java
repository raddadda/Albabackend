package com.jobstore.jobstore.repository.work;

import com.jobstore.jobstore.entity.Contents;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ContentRepository extends JpaRepository <Contents,Long>  {


    @Override
    List<Contents> findAll();

    Optional<Contents> findByContentsid (long contentsid);

    // SELECT * FROM jobstore.contents WHERE workid = 1  ORDER BY contentsid DESC;

    List<Contents> findByWorkWorkid(long workid, Sort sort);

    @Transactional
    @Modifying // 조회 X
    @Query("DELETE FROM Contents c WHERE c.contentsid = :id")
    int deletebyContentsid(@Param("id") long id);


    @Transactional
    @Modifying // 조회 X
    @Query("DELETE FROM Contents c WHERE c.work.workid = :id")
    int deletebyWorkid(@Param("id") long id);

}
