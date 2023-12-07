package com.jobstore.jobstore.controller;

import com.jobstore.jobstore.dto.FindPasswordDto;
import com.jobstore.jobstore.dto.LoginDto;
import com.jobstore.jobstore.dto.TokenDto;
import com.jobstore.jobstore.dto.request.attendance.AttendanceDto;
import com.jobstore.jobstore.dto.request.auth.TokenValidationDto;
import com.jobstore.jobstore.dto.request.member.AdminjoinDto;
import com.jobstore.jobstore.dto.request.member.DoubleCheckDto;
import com.jobstore.jobstore.dto.request.member.UserjoinDto;
import com.jobstore.jobstore.dto.response.ResultDto;
import com.jobstore.jobstore.dto.response.auth.LoginResponseDto;
import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.jwt.JwtTokenUtil;
import com.jobstore.jobstore.repository.MemberRepository;
import com.jobstore.jobstore.repository.StoreRepository;
import com.jobstore.jobstore.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

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
    @PostMapping("/doublecheck")
    @Operation(summary = "memberid 중복확인검사", description = "admin 전용 회원가입입니다.")
    public ResponseEntity<ResultDto<Object>> doublecheck(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
            content = @Content(schema=@Schema(implementation = DoubleCheckDto.class)))@RequestBody DoubleCheckDto doubleCheckDto){
        try{
            if(!memberService.duplicateMemberid(doubleCheckDto.getMemberid())){
                return ResponseEntity.ok(ResultDto.of("resultcode","사용가능한 아이디입니다.",doubleCheckDto));
            }
            return  ResponseEntity.ok(ResultDto.of("resultcode","아이디가 중복이 됩니다",null));
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //admin
    @PostMapping("/admin/join")
    @Operation(summary = "admin 회원가입", description = "admin 전용 회원가입입니다.")
    public ResponseEntity<ResultDto<Object>> joinAdmin(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
            content = @Content(schema=@Schema(implementation = AdminjoinDto.class)))
        @RequestBody AdminjoinDto adminjoinDto
    ) {
        try{
            //ID 중복체크
            if(!memberService.duplicateMemberid(adminjoinDto.getMemberid())) {
                boolean joinAdmin = memberService.joinAdmin(adminjoinDto);
                if(joinAdmin){
                    return ResponseEntity.ok(ResultDto.of("resultCode","회원가입이 성공했습니다.", adminjoinDto));
                }
                return ResponseEntity.ok(ResultDto.of("resultCode","사업자 등록번호가 이미 존재합니다.", null));
            }
            return ResponseEntity.ok(ResultDto.of("resultCode","회원가입이 실패했습니다.", null));
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
    //user
    @PostMapping("/user/join")
    @Operation(summary = "user 회원가입", description = "user 전용 회원가입입니다.")
    public ResponseEntity<ResultDto<Object>> joinUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = UserjoinDto.class)))
            @RequestBody UserjoinDto userjoinDto
    ) {
        try {
            //ID 중복체크
            if(!memberService.duplicateMemberid(userjoinDto.getMemberid())) {
                if(memberService.joinUser(userjoinDto)){
                    return ResponseEntity.ok(ResultDto.of("resultCode","회원가입이 성공했습니다.", userjoinDto));
                }else {
                    return ResponseEntity.ok(ResultDto.of("resultCode2","회원가입이 실패(초대코드 인증 실패)", null));
                }

            }
            return ResponseEntity.ok(ResultDto.of("resultCode","회원가입이 실패했습니다.", null));
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
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
        try{
            if(loginDto.getRole().equals("ADMIN")){
                //memberRepository.existsByMemberidAndStoreid(loginDto.getMemberid())){
                Member member = memberService.login(loginDto);
                String id = member.getMemberid();
                if (!memberService.findByMemberidToRole(id).equals("ADMIN")) {
                    return ResponseEntity.ok(ResultDto.of("resultCode","사업자에 해당 아이디가 존재하지 않습니다.", null));
                }
                if (member == null) {
                    return ResponseEntity.ok(ResultDto.of("403", "로그인 아이디 또는 비밀번호가 틀렸습니다.", null));
                }
                LoginResponseDto response = memberService.loginToken(member);
                if(response == null){
                    return ResponseEntity.ok(ResultDto.of("403", "이미 로그인이 되어있는 아이디입니다", null));
                }
                return ResponseEntity.ok(ResultDto.of("200", "로그인 성공.", response));
            }else if(loginDto.getRole().equals("USER")){
                Member member = memberService.login(loginDto);
                String id = member.getMemberid();
                if (!memberService.findByMemberidToRole(id).equals("USER")) {
                    return ResponseEntity.ok(ResultDto.of("resultCode","구직자에 해당 아이디가 존재하지 않습니다.", null));
                }
                if (member == null) {
                    return ResponseEntity.ok(ResultDto.of("403", "로그인 아이디 또는 비밀번호가 틀렸습니다.", null));
                }
                LoginResponseDto response = memberService.loginToken(member);
                if(response == null){
                    return ResponseEntity.ok(ResultDto.of("403", "이미 로그인이 되어있는 아이디입니다", null));
                }
                return ResponseEntity.ok(ResultDto.of("200", "로그인 성공.", response));
            }
            return ResponseEntity.ok(ResultDto.of("404", "해당 role이 존재하지 않음", null));
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/refreshToken")
    @Operation(summary = "토큰 재발급", description = "로그인상태에서 토큰을 재발급 합니다")
    public ResponseEntity<ResultDto<Object>> refreshToken(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = TokenDto.class)))
            @RequestBody TokenDto tokenDto
    ){
        try {
            Member member = memberRepository.findByMemberid2(tokenDto.getMemberid());
            if(member == null){
                return  ResponseEntity.ok(ResultDto.of("resultcode","존재하지않는 멤버입니다.",null));
            }
            LoginResponseDto response = memberService.refreshToken(member,tokenDto.getToken());
            if(response == null){
                return  ResponseEntity.ok(ResultDto.of("resultcode","token재발급 실패",response));
            }

            return  ResponseEntity.ok(ResultDto.of("resultcode","token재발급 완료",response));
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
    @PostMapping("/tokenValidation")
    @Operation(summary = "토큰 재발급", description = "로그인상태에서 토큰을 재발급 합니다")
    public ResponseEntity<ResultDto<Object>> validToken(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = TokenValidationDto.class)))
            @RequestBody TokenValidationDto tokenDto
    ){
        try {
            String token = tokenDto.getToken();
            boolean valid = memberService.validToken(token);
            return  ResponseEntity.ok(ResultDto.of("resultcode","token 검증 완료",valid));
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }
    /**
     비밀번호 찾기
     */
    @PostMapping("/findPassword")
    @Operation(summary = "password 찾기폼", description = "password찾기")
    public ResponseEntity<ResultDto<Object>> createAttendance(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema = @Schema(implementation = FindPasswordDto.class)))
            @RequestBody FindPasswordDto findPasswordDto
    ) {
        System.out.println("-----------------findPasswordDto-----------------");
        boolean result = memberService.findPassword(findPasswordDto);
        if(!result){
            return ResponseEntity.ok(ResultDto.of("실패", "비밀번호 찾기에 실패", result));
        }
        return ResponseEntity.ok(ResultDto.of("성공", "비밀번호 찾기에 성공", result));

    }
}
