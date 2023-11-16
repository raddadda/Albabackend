package com.jobstore.jobstore.Controller;

import com.jobstore.jobstore.dto.LoginDto;
import com.jobstore.jobstore.dto.MemberDto;
import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.jwt.JwtTokenUtil;
import com.jobstore.jobstore.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/jwt-login")
public class JwtLoginApiController {
    private final MemberService memberService;
    @PostMapping("/jwt-login/join")
    public String join(@RequestBody MemberDto memberDto) {

//        // loginId 중복 체크
//        if(memberService.checkLoginIdDuplicate(joinRequest.getLoginId())) {
//            return "로그인 아이디가 중복됩니다.";
//        }
//        // 닉네임 중복 체크
//        if(userService.checkNicknameDuplicate(joinRequest.getNickname())) {
//            return "닉네임이 중복됩니다.";
//        }
//        // password와 passwordCheck가 같은지 체크
//        if(!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
//            return"바밀번호가 일치하지 않습니다.";
//        }

        memberService.join(memberDto);
        return "회원가입 성공";
    }

    @PostMapping("/jwt-login/login")
    public String login(@RequestBody LoginDto loginDto) {

        Member member = memberService.login(loginDto);

        // 로그인 아이디나 비밀번호가 틀린 경우 global error return
        if(member == null) {
            return"로그인 아이디 또는 비밀번호가 틀렸습니다.";
        }
        // 로그인 성공 => Jwt Token 발급
        String secretKey = "my-secret-key-123123";
        long expireTimeMs = 1000 * 60 * 60;     // Token 유효 시간 = 60분
        String jwtToken = JwtTokenUtil.createToken(member.getMemberid(), secretKey, expireTimeMs);
        return jwtToken;
    }

//    @GetMapping("/info")
//    public String userInfo(Authentication auth) {
//        User loginUser = userService.getLoginUserByLoginId(auth.getName());
//
//        return String.format("loginId : %s\nnickname : %s\nrole : %s",
//                loginUser.getLoginId(), loginUser.getNickname(), loginUser.getRole().name());
//    }
//
//    @GetMapping("/admin")
//    public String adminPage() {
//        return "관리자 페이지 접근 성공";
//    }
}
