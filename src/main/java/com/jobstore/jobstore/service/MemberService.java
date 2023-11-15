package com.jobstore.jobstore.service;


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
}
