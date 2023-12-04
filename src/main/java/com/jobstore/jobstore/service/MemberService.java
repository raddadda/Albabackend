package com.jobstore.jobstore.service;


import com.jobstore.jobstore.dto.FindIdDto;
import com.jobstore.jobstore.dto.FindPasswordDto;
import com.jobstore.jobstore.dto.LoginDto;
import com.jobstore.jobstore.dto.request.member.*;
import com.jobstore.jobstore.dto.MemberDto;
import com.jobstore.jobstore.dto.response.auth.LoginResponseDto;
import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.entity.Payment;
import com.jobstore.jobstore.entity.Store;
import com.jobstore.jobstore.jwt.JwtTokenFilter;
import com.jobstore.jobstore.jwt.JwtTokenUtil;
import com.jobstore.jobstore.repository.MemberRepository;
import com.jobstore.jobstore.repository.PaymentRepository;
import com.jobstore.jobstore.repository.StoreRepository;
import com.jobstore.jobstore.utill.AwsUtill;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.tools.JavaFileManager;
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
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;
    //MailBodyUtil mailBodyUtil = new MailBodyUtil();

    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    /**
     회원가입
     */
    //admin

    public boolean joinAdmin(AdminjoinDto adminjoinDto){
        Store storeEntity = new Store();
        boolean existCompanynumber=storeRepository.existsByCompanynumber(adminjoinDto.getCompanynumber());
        storeEntity.setCompanyname(adminjoinDto.getCompanyname());
        storeEntity.setCeo(adminjoinDto.getCeo());
        storeEntity.setCompanynumber(adminjoinDto.getCompanynumber());

        Member memberEntity = adminjoinDto.toEntity(passwordEncoder.encode(adminjoinDto.getPassword()));
        memberEntity.setStore(storeEntity); // Store와의 연관 설정


        if(!existCompanynumber) {
            storeRepository.save(storeEntity);
            memberRepository.save(memberEntity);
            return true;
        }else{
            return false;
        }

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
    //한명에대한 조회
    public MemberAndStoreDetailsDto findMemberDetails(String memberid){
        // System.out.println("asdsadasdasdas:       "+memberid);
        Member member=memberRepository.findByMemberid2(memberid);
        long storeidletsgo=memberRepository.findeByMemberidForStoreid(memberid);

        // System.out.println("sadadasdasdasdasd:     "+storeidletsgo);
        Store store=storeRepository.findByStoreid2(storeidletsgo);
        return new MemberAndStoreDetailsDto(member,store);
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

    /**
     회원 수정
     */
    public Member updateMember(MemberUpdateDto memberDto){
        Member existingMember = memberRepository.findByMemberid(memberDto.getMemberid())
                .orElseThrow(() -> new RuntimeException("해당 멤버아이디는 존재하지 않는 멤버 아이디입니다"));
        existingMember.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        existingMember.setPhonenumber(memberDto.getPhonenumber());
        existingMember.setName(memberDto.getName());
        return memberRepository.save(existingMember);
    }

    /**
     * 비밀번호로 수정
     */
    public Member updateMemberPassword(String memberid,String password){
        Member existingMember = memberRepository.findByMemberid(memberid)
                .orElseThrow(() -> new RuntimeException("해당 멤버아이디는 존재하지 않는 멤버 아이디입니다"));
        existingMember.setPassword(passwordEncoder.encode(password));
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

        try {

           String imageUrl = awsUtill.upload(multipartFile);

           if (imageUploadDto.getRole().equals("USER")) { // 유저일 때

               Member existingMember = memberRepository.findByMemberid(imageUploadDto.getMemberid())
                       .orElseThrow(() -> new RuntimeException("해당 멤버아이디는 존재하지 않는 멤버 아이디입니다"));

               if ( existingMember.getMemberimg() != null  && existingMember.getMemberimg().contains("https://")) {
                   String [] url = existingMember.getMemberimg().split("amazonaws.com");
                   if (!url[1].isEmpty()){
                       awsUtill.delete(url[1].substring(1));
                   }
               }

               existingMember.setMemberimg(imageUrl);
               memberRepository.save(existingMember);
               return imageUrl;

           } else {
               Store existingStore = storeRepository.findByStoreid(imageUploadDto.getStoreid())
                       .orElseThrow(() -> new RuntimeException("해당 멤버아이디는 존재하지 않는 멤버 아이디입니다"));

               if (existingStore.getCompanyimg() != null && existingStore.getCompanyimg().contains("https://")) {
                   String [] url = existingStore.getCompanyimg().split("amazonaws.com");

                   if (!url[1].isEmpty()){
                       awsUtill.delete(url[1].substring(1));
                   }
               }
               existingStore.setCompanyimg(imageUrl);
               storeRepository.save(existingStore);
               return imageUrl;
           }

        } catch ( Exception e) {
            return "";
        }
    }
    public String findByMemberidToRole(String memberid){
        return memberRepository.findByMemberidToRole(memberid);
    }


    public HashMap findByWorker(long storeid){

        List<Member> member = memberRepository.findeByMemberidForStoreid2(storeid);
        HashMap<String,String> hashMap = new HashMap<>();
        if(member!=null){
            for(Member list : member){
                if(list.getRole().equals("USER")){
                    String key = list.getMemberid();
                    String value = list.getName();
                    hashMap.put(key,value);
                }
            }

           return hashMap;

        }
        return null;
    }


    public LoginResponseDto loginToken(Member member){

        // 로그인 성공 => Jwt Token 발급
        System.out.println("11122");
        String secretKey = "my-secret-key-123123";
        long expireTimeMs = 1000 * 60 * 60;     // Token 유효 시간 = 60분

        String jwtToken = JwtTokenUtil.createToken(member.getMemberid(), secretKey, expireTimeMs,member.getStore().getStoreid());
        String refreshToken = JwtTokenUtil.refreshToken(member.getMemberid(), secretKey, expireTimeMs,member.getStore().getStoreid());
        System.out.println("refreshToken"+refreshToken);
        member.setRefreshtoken(refreshToken);
        member.setIslogin(1);
        memberRepository.save(member);

        LoginResponseDto reposonse = new LoginResponseDto();
        reposonse.setToken(jwtToken);
        reposonse.setMemberid(member.getMemberid());
        reposonse.setStoreid(member.getStore().getStoreid());

        return reposonse;


    }
    public LoginResponseDto refreshToken(Member member,String token){
        // 로그인 성공 => Jwt Token 발급
        String secretKey = "my-secret-key-123123";
        long expireTimeMs = 1000 * 60 * 60;     // Token 유효 시간 = 60분
        String jwtToken = JwtTokenUtil.refreshToken(member.getMemberid(), secretKey, expireTimeMs,member.getStore().getStoreid());
        if(member.getRefreshtoken().equals(token)){
            member.setRefreshtoken(jwtToken);
            memberRepository.save(member);

            LoginResponseDto reposonse = new LoginResponseDto();
            reposonse.setToken(jwtToken);
            reposonse.setMemberid(member.getMemberid());
            reposonse.setStoreid(member.getStore().getStoreid());

            return reposonse;
        }
        return null;

    }

    public boolean validToken(String token){
        String secretKey = "my-secret-key-123123";
        //Jwt 디코딩

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            //Jwt 디코딩된 클레임
            String username = claims.getSubject();

            if(claims == null){
                return false;
            }
            return true;

        } catch (Exception e){
            return false;
        }


        //if(token)
        //Token 재발급
//        public static String refreshToken(String loginId, String key, long expireTimeMs,long storeid){
//            Claims claims = Jwts.claims();
//            claims.put("memberid",loginId);
//            claims.put("storeid",storeid);
//            claims.put("isRefresh",true);
//            return Jwts.builder()
//                    .setClaims(claims)
//                    .setIssuedAt(new Date(System.currentTimeMillis()))
//                    .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
//                    .signWith(SignatureAlgorithm.HS256, key)
//                    .compact();
//
//        }
    }

    public String setPassword(int length) {
        int index = 0;
        char[] charArr = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
                'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
                'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
                'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                'w', 'x', 'y', 'z' };

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < length; i++) {
            index = (int) (charArr.length * Math.random());
            sb.append(charArr[index]);
        }

        return sb.toString();
    }






    public boolean findPassword(FindPasswordDto findPasswordDto){
        Member member = memberRepository.findByMemberid2(findPasswordDto.getMemberid());
        if(member == null){
            return false;
        }
        String tempPassword = setPassword(7);
//gCQ33M8
        //member.setPassword(tempPassword);
        updateMemberPassword(member.getMemberid(),tempPassword);
        String content = "";
        System.out.println("before:"+member.getPassword());
        System.out.println("after:"+tempPassword);
        //if(member.getPassword().equals(tempPassword)){
            content = tempPassword;
        //}
        String subject = "test 메일";
        String from = "cyc123m@naver.com";
        String to = findPasswordDto.getEmail();
                //"받는이 아이디@도메인주소";
        //if(!duplicateMemberid(member.getMemberid())){
            //String password = memberRepository
        System.out.println("@@@@@@");
            try {
                System.out.println("1");
                MimeMessage mail = javaMailSender.createMimeMessage();
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mail,true,"UTF-8");
                System.out.println("2");
                mimeMessageHelper.setFrom(from);
                mimeMessageHelper.setTo(to);
                mimeMessageHelper.setSubject(subject);
                mimeMessageHelper.setText(content, true);
                System.out.println("3");
                if(mimeMessageHelper == null){
                    return false;
                }
                javaMailSender.send(mail);
                return true;
            }catch (MessagingException e){
                e.printStackTrace();
            }


        //}
        return false;
    }

//    public boolean findId(FindIdDto findIdDto){
//        Member member = memberRepository.existsByName(findIdDto.getMemberid());
//        if(member == null){
//            return false;
//        }
//        String tempPassword = setPassword(7);
////gCQ33M8
//        //member.setPassword(tempPassword);
//        updateMemberPassword(member.getMemberid(),tempPassword);
//        String content = "";
//        System.out.println("before:"+member.getPassword());
//        System.out.println("after:"+tempPassword);
//        //if(member.getPassword().equals(tempPassword)){
//        content = tempPassword;
//        //}
//        String subject = "test 메일";
//        String from = "cyc123m@naver.com";
//        String to = findIdDto.getEmail();
//        //"받는이 아이디@도메인주소";
//        //if(!duplicateMemberid(member.getMemberid())){
//        //String password = memberRepository
//        System.out.println("@@@@@@");
//        try {
//            System.out.println("1");
//            MimeMessage mail = javaMailSender.createMimeMessage();
//            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mail,true,"UTF-8");
//            System.out.println("2");
//            mimeMessageHelper.setFrom(from);
//            mimeMessageHelper.setTo(to);
//            mimeMessageHelper.setSubject(subject);
//            mimeMessageHelper.setText(content, true);
//            System.out.println("3");
//            if(mimeMessageHelper == null){
//                return false;
//            }
//            javaMailSender.send(mail);
//            return true;
//        }catch (MessagingException e){
//            e.printStackTrace();
//        }
//
//
//        //}
//        return false;
//    }


}
