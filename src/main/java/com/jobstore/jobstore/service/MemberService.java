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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
    public List<MemberDto> findAllMember(){
        List<Member> result=memberRepository.findAll();
        List<MemberDto> list =new ArrayList<MemberDto>();
        for(int i=0;i<result.size();i++){
            MemberDto data=new MemberDto();
            data.setMemberid(result.get(i).getMemberid());
            data.setPassword(result.get(i).getPassword());
            data.setPhonenumber(result.get(i).getPhonenumber());
            data.setName(result.get(i).getName());
            data.setRole(result.get(i).getName());
            data.setMemberimg(result.get(i).getMemberimg());
            list.add(data);
        }
        return list;
    }
    //멤버id존재유무에 따라 memberid가 일치하면 다른정보 수정이 가능하다.
    //그리고 수정이 완료되면 그 수정이 완료된 객체를 반환
    //프론트에서 보기편하게^^
    //수정
    public Member updateMember(MemberDto memberDto){
        Member existingMember = memberRepository.findByMemberid(memberDto.getMemberid())
                .orElseThrow(() -> new RuntimeException("해당 멤버아이디는 존재하지 않는 멤버 아이디입니다"));
        existingMember.setPassword(memberDto.getPassword());
        existingMember.setPhonenumber(memberDto.getPhonenumber());
        existingMember.setName(memberDto.getName());
        return memberRepository.save(existingMember);
    }
    //아이디가 존재하면 행의갯수는 0보다크니까 삭제성공
    //존재하지 않으면 행의갯수는 0이니까 삭제 실패
    public String deleteBymemberid(String memberid) {
        int deletedRows = memberRepository.deleteByMemberid(memberid);
        if (deletedRows > 0) {
            return memberid + "님 탈퇴 성공";
        } else {
            return "삭제하고자하는 멤버아이디 정보가 없습니다.";
        }
    }
}
