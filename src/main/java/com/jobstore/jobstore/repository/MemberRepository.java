package com.jobstore.jobstore.repository;

import com.jobstore.jobstore.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,String> {
    Optional<Member> findByMemberid(String memberid);
    //Optional<User> findByUserid(String userid);
}
