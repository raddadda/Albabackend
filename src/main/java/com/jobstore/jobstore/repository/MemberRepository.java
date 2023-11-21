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



    @Query("SELECT m FROM Member m WHERE m.memberid = :memberid")
    Member findByMemberid2(@Param("memberid") String memberid);
    Optional<Member> findOneWithAuthByMemberid(String memberid);
    //Optional<User> findByUserid(String userid);

    boolean existsByMemberid(String memberid);

    //memberid기반삭제
    @Transactional // transaction 내부
    @Modifying // 조회 x
    @Query("DELETE FROM Member m WHERE m.memberid = :memberid")
    int deleteByMemberid(@Param("memberid") String memberid);

}
