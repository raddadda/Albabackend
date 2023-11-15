package com.jobstore.jobstore.Controller;

import com.jobstore.jobstore.dto.MemberDto;
import com.jobstore.jobstore.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Tag(name = "Member", description = "Member CRUD")
public class MemberController {
    @Autowired
    MemberService memberService;
    //회원가입
    @GetMapping("/join")
    @Operation(summary = "문자열 반복", description = "파라미터로 받은 문자열을 2번 반복합니다.")
    public String joinMember(Model model){
        model.addAttribute("memberDto",new MemberDto());
        return "join";
    }
    @PostMapping("/join")
    public String createMember(MemberDto memberDto){
        memberService.join(memberDto);
        System.out.println("컨트롤러");
        return "redirect:/";
    }
    //로그인
    @GetMapping("/login")
    public String loginMember(Model model){
        // model.addAttribute("loginDto",new loginDto());
        return "login";
    }
}
