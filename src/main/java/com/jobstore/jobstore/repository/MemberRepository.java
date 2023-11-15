package com.jobstore.jobstore.repository;

import com.jobstore.jobstore.entity.Admin;
import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,String> {
    Optional<Member> findByMemberId(String memberid);
    //Optional<User> findByUserid(String userid);
}
