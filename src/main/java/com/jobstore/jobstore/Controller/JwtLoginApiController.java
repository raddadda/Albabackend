package com.jobstore.jobstore.Controller;

import com.jobstore.jobstore.dto.LoginDto;
import com.jobstore.jobstore.dto.MemberDto;
import com.jobstore.jobstore.dto.RequestDto;
import com.jobstore.jobstore.dto.StoreDto;
import com.jobstore.jobstore.dto.request.AdminjoinDto;
import com.jobstore.jobstore.dto.request.UserjoinDto;
import com.jobstore.jobstore.dto.response.AuthresponseDto;
import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.jwt.JwtTokenUtil;
import com.jobstore.jobstore.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;


import java.util.Map;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/jwt-login")
@Tag(name = "Auth", description = "Auth CRUD")
public class JwtLoginApiController {

    private final MemberService memberService;

    /**
     회원가입
     */

    //admin
    @PostMapping("/admin/join")
    @Operation(summary = "admin 회원가입", description = "admin 전용 회원가입입니다.")
    public ResponseEntity joinAdmin(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
            content = @Content(schema=@Schema(implementation = AdminjoinDto.class)))

        @RequestBody AdminjoinDto adminjoinDto
    ) {
        memberService.joinAdmin(adminjoinDto);
        return ResponseEntity.ok("성공");
    }
    //user
    @PostMapping("/user/join")
    @Operation(summary = "user 회원가입", description = "user 전용 회원가입입니다.")
    public ResponseEntity joinUser(

            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = UserjoinDto.class)))

            @RequestBody UserjoinDto userjoinDto
    ) {
        memberService.joinUser(userjoinDto);
        return ResponseEntity.ok("성공");
    }

    /**
     로그인
     */

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "user, admin 공통 로그인")
    public String login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = LoginDto.class)))


            @RequestBody LoginDto loginDto) {


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
