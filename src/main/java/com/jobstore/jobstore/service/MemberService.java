package com.jobstore.jobstore.service;


import com.jobstore.jobstore.dto.LoginDto;
import com.jobstore.jobstore.dto.MemberDto;
import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.repository.MemberRepository;
import com.jobstore.jobstore.repository.StoreRepository;
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

    @Autowired
    private StoreRepository storeRepository;
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
        if (loginId == null) return null;

        Optional<Member> optionalUser = memberRepository.findByMemberid(loginId);
        if (optionalUser.isEmpty()) return null;

        return optionalUser.get();
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
        existingMember.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        existingMember.setPhonenumber(memberDto.getPhonenumber());
        existingMember.setName(memberDto.getName());
        return memberRepository.save(existingMember);
    }
    //아이디가 존재하면 행의갯수는 0보다크니까 삭제성공
    //존재하지 않으면 행의갯수는 0이니까 삭제 실패
    //admin유저 삭제
    public String deleteBymemberid(String memberid,long storeid) {
        int deletedRows = memberRepository.deleteByMemberid(memberid);
        int storeDelteRows=storeRepository.deleteByStoreid(storeid);
        if (deletedRows > 0 && storeDelteRows > 0) {
            return memberid + "님 탈퇴 성공";
        } else if (deletedRows > 0 && storeDelteRows > 0) {
            return "잘못된 접근 방식입니다.";
        } else {
            return "삭제하고자하는 멤버아이디 정보가 없습니다.";
        }
    }
    //일반유저 삭제
    public String deleteByUserto_memberid(String memberid) {
        int deletedRows=memberRepository.deleteByMemberid(memberid);
        if(deletedRows > 0){
            return "유저: "+memberid+"님 탈퇴 성공";
        }else{
            return "삭제하고자하는 유저정보가 없습니다";
        }
    }
}
