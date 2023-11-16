package com.jobstore.jobstore.repository;

import com.jobstore.jobstore.entity.Member;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,String> {
    Optional<Member> findByMemberid(String memberid);



    //memberid기반삭제
    @Transactional
    @Modifying
    @Query("DELETE FROM Member m WHERE m.memberid = :memberid")
    int deleteByMemberid(@Param("memberid") String memberid);
}
