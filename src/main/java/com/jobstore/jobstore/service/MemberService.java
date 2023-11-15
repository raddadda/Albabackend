package com.jobstore.jobstore.service;


import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.repository.MemberRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service

public class MemberService  {

    @Autowired
    private MemberRepository memberRepository;


}
