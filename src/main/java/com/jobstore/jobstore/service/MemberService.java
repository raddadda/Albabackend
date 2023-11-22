package com.jobstore.jobstore.service;


import com.jobstore.jobstore.dto.LoginDto;
import com.jobstore.jobstore.dto.MemberDto;
import com.jobstore.jobstore.dto.request.AdminjoinDto;
import com.jobstore.jobstore.dto.request.ImageUploadDto;
import com.jobstore.jobstore.dto.request.UserjoinDto;
import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.entity.Payment;
import com.jobstore.jobstore.entity.Store;
import com.jobstore.jobstore.repository.MemberRepository;
import com.jobstore.jobstore.repository.PaymentRepository;
import com.jobstore.jobstore.repository.StoreRepository;
import com.jobstore.jobstore.utill.AwsUtill;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class MemberService  {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private AwsUtill awsUtill;

    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    /**
     회원가입
     */
    //admin

    public void joinAdmin(AdminjoinDto adminjoinDto){
        Store storeEntity = new Store();
        storeEntity.setCompanyname(adminjoinDto.getCompanyname());
        storeEntity.setCeo(adminjoinDto.getCeo());
        storeEntity.setCompanynumber(adminjoinDto.getCompanynumber());
        storeEntity.setCompanyimg(adminjoinDto.getCompanyimg());
        storeRepository.save(storeEntity);

        Member memberEntity = adminjoinDto.toEntity(passwordEncoder.encode(adminjoinDto.getPassword()));
        memberEntity.setStore(storeEntity); // Store와의 연관 설정
        memberRepository.save(memberEntity);

    }
    //join
    public boolean joinUser(UserjoinDto userjoinDto){
        Store store = storeRepository.findByInvitecode(userjoinDto.getInvitecode());
        //초대코드 유효성 검사
        if (store != null) {
            Member member = userjoinDto.toEntity(passwordEncoder.encode(userjoinDto.getPassword()));
            member.setStore(store);
            memberRepository.save(member);
            return true;
        }
        return false;
    }

    public boolean duplicateMemberid(String memberid) {
        return memberRepository.existsByMemberid(memberid);
    }
    /**
     로그인
     */
    public Member login(LoginDto loginDto) {
        Optional<Member> optionalUser = memberRepository.findByMemberid(loginDto.getMemberid());

        // loginId와 일치하는 User가 없으면 null return
        if(optionalUser.isEmpty()) {
            return null;
        }

        Member member = optionalUser.get();

        // 찾아온 User의 password와 입력된 password가 다르면 null return
        if(!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) { return null; }

        return member;
    }

    public Member getLoginUserByLoginId(String loginId) {
        if (loginId == null) return null;

        Optional<Member> optionalUser = memberRepository.findByMemberid(loginId);
        if (optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }

    /**
    회원 전체 조회
     */
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

    /**
     회원 수정
     */
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

    /**
     회원 삭제
     */
    //admin유저 삭제
    public String deleteBymemberid(String memberid,long storeid) {
        paymentRepository.deleteByMemberIdAndStoreId(memberid, storeid);
        int deletedRows = memberRepository.deleteByMemberid(memberid);
        int storeDelteRows=storeRepository.deleteByStoreid(storeid);
        if (deletedRows > 0 && storeDelteRows > 0) {
            return memberid + "님 탈퇴 성공";
        } else if (deletedRows <= 0 && storeDelteRows <= 0) {
            return "삭제하고자하는 멤버아이디 정보가 없습니다.";
        } else {
            return "잘못된 접근 방식입니다.";
        }
    }
    //일반유저 삭제
    public String deleteByUserto_memberid(String memberid) {
        deleteMemberAndRelatedPayments(memberid);
        int deletedRows=memberRepository.deleteByMemberid(memberid);
        if(deletedRows > 0){
            return "유저: "+memberid+"님 탈퇴 성공";
        }else{
            return "삭제하고자하는 유저정보가 없습니다";
        }
    }


    @Transactional
    public void deleteMemberAndRelatedPayments(String memberid) {
        Optional<Member> member = memberRepository.findById(memberid);

        if (member.isPresent()) {
            Member foundMember = member.get();
            List<Payment> payments = foundMember.getPayments();
            for (Payment payment : payments) {
                paymentRepository.delete(payment);
            }
        }
    }


    // 이미지 업로드

    public String ImageUpdate (MultipartFile multipartFile, ImageUploadDto imageUploadDto) {

        String dirname = "profileImage";

        try {

           String imageUrl = awsUtill.upload(multipartFile ,dirname);

           if (imageUploadDto.getStoreid() == 0) { // 멤버 아이디

               Member existingMember = memberRepository.findByMemberid(imageUploadDto.getMemberid())
                       .orElseThrow(() -> new RuntimeException("해당 멤버아이디는 존재하지 않는 멤버 아이디입니다"));

               existingMember.setMemberimg(imageUrl);

               if (!existingMember.getMemberimg().equals("")) {
                   String [] url = existingMember.getMemberimg().split("amazonaws.com");
                   if (!url[1].isEmpty()){
                       awsUtill.delete(url[1].substring(1));
                   }
               }
               memberRepository.save(existingMember);

           } else {

               Store existingStore = storeRepository.findByStoreid(imageUploadDto.getStoreid())
                       .orElseThrow(() -> new RuntimeException("해당 멤버아이디는 존재하지 않는 멤버 아이디입니다"));
               if (!existingStore.getCompanyimg().equals("")) {
                   String [] url = existingStore.getCompanyimg().split("amazonaws.com");
                   if (!url[1].isEmpty()){
                       awsUtill.delete(url[1].substring(1));
                   }
               }
               existingStore.setCompanyimg(imageUrl);
               storeRepository.save(existingStore);
           }
            return imageUrl;
        } catch ( Exception e) {
            return "";
        }
    }
    public String findByMemberidToRole(String memberid){
        return memberRepository.findByMemberidToRole(memberid);
    }

}
