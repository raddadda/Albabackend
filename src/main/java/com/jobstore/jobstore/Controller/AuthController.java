package com.jobstore.jobstore.Controller;

import com.jobstore.jobstore.dto.LoginDto;
import com.jobstore.jobstore.dto.request.AdminjoinDto;
import com.jobstore.jobstore.dto.request.UserjoinDto;
import com.jobstore.jobstore.dto.response.ResultDto;
import com.jobstore.jobstore.dto.response.auth.LoginResponseDto;
import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.entity.Store;
import com.jobstore.jobstore.jwt.JwtTokenUtil;
import com.jobstore.jobstore.repository.MemberRepository;
import com.jobstore.jobstore.repository.StoreRepository;
import com.jobstore.jobstore.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Auth CRUD")
public class AuthController {

    private final MemberService memberService;
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    /**
     회원가입
     */

    //admin
    @PostMapping("/admin/join")
    @Operation(summary = "admin 회원가입", description = "admin 전용 회원가입입니다.")
    public ResponseEntity<ResultDto<Object>> joinAdmin(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
            content = @Content(schema=@Schema(implementation = AdminjoinDto.class)))
        @RequestBody AdminjoinDto adminjoinDto
    ) {
        //ID 중복체크
        if(!memberService.duplicateMemberid(adminjoinDto.getMemberid())) {
            memberService.joinAdmin(adminjoinDto);
            return ResponseEntity.ok(ResultDto.of("resultCode","회원가입이 성공했습니다.", adminjoinDto));
        }
        return ResponseEntity.ok(ResultDto.of("resultCode","회원가입이 실패했습니다.", null));
    }
    //user
    @PostMapping("/user/join")
    @Operation(summary = "user 회원가입", description = "user 전용 회원가입입니다.")
    public ResponseEntity<ResultDto<Object>> joinUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = UserjoinDto.class)))
            @RequestBody UserjoinDto userjoinDto
    ) {
        //ID 중복체크
        if(!memberService.duplicateMemberid(userjoinDto.getMemberid())) {
            if(memberService.joinUser(userjoinDto)){
                return ResponseEntity.ok(ResultDto.of("resultCode","회원가입이 성공했습니다.", userjoinDto));
            }else {
                return ResponseEntity.ok(ResultDto.of("resultCode2","회원가입이 실패(초대코드 인증 실패)", null));
            }

        }
        return ResponseEntity.ok(ResultDto.of("resultCode","회원가입이 실패했습니다.", null));
    }

    /**
     로그인
     */

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "user, admin 공통 로그인")

    public ResponseEntity<ResultDto<LoginResponseDto>> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = LoginDto.class)))
            @RequestBody LoginDto loginDto) {

        Member member = memberService.login(loginDto);

        // 로그인 아이디나 비밀번호가 틀린 경우 global error return
        if (member == null) {
            return ResponseEntity.ok(ResultDto.of("403", "로그인 아이디 또는 비밀번호가 틀렸습니다.", null));
        }

        // 로그인 성공 => Jwt Token 발급
        String secretKey = "my-secret-key-123123";
        long expireTimeMs = 1000 * 60 * 60;     // Token 유효 시간 = 60분
        String jwtToken = JwtTokenUtil.createToken(member.getMemberid(), secretKey, expireTimeMs);

        LoginResponseDto reposonse = new LoginResponseDto();
        reposonse.setToken(jwtToken);
        reposonse.setMemberid(member.getMemberid());
        reposonse.setStoreid(member.getStore().getStoreid());

        return ResponseEntity.ok(ResultDto.of("200", "로그인 성공.", reposonse));

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
