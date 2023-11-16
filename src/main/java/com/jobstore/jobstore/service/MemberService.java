package com.jobstore.jobstore.service;


import com.jobstore.jobstore.dto.LoginDto;
import com.jobstore.jobstore.dto.MemberDto;
import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.repository.MemberRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class MemberService  {

    @Autowired
    private MemberRepository memberRepository;

    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public void join(MemberDto memberDto){
        System.out.println("엔코더 전"+memberDto.getPassword());
        System.out.println("엔코더 후"+passwordEncoder.encode(memberDto.getPassword()));

        memberRepository.save(memberDto.toEntity(passwordEncoder.encode(memberDto.getPassword())));

    }
    public Member login(LoginDto loginDto) {
        System.out.println("id : "+loginDto.getMemberid());
        System.out.println("pw : "+loginDto.getPassword());
        Optional<Member> optionalUser = memberRepository.findByMemberid(loginDto.getMemberid());

        // loginId와 일치하는 User가 없으면 null return
        if(optionalUser.isEmpty()) {
            return null;
        }

        Member member = optionalUser.get();

        // 찾아온 User의 password와 입력된 password가 다르면 null return
        System.out.println("레포지토리 조회 결과 : "+optionalUser);
        if(!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) { return null; }
        System.out.println("2222");
//        if(!member.getPassword().equals(loginDto.getPassword())) {
//            return null;
//        }

        return member;
    }

    public Member getLoginUserByLoginId(String loginId) {
        if(loginId == null) return null;

        Optional<Member> optionalUser = memberRepository.findByMemberid(loginId);
        if(optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }
}
