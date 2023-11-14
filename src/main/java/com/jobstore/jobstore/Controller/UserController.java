package com.jobstore.jobstore.Controller;

import com.jobstore.jobstore.dto.UserDto;
import com.jobstore.jobstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired UserService userService;

    //구직자 회원가입
    @GetMapping("/user/join")
    public String joinUser(Model model){
        model.addAttribute("userDto",new UserDto());
        return "userJoin";
    }
    @PostMapping("/user/join")
    public String createUser(UserDto userDto){
        //userService.join(userDto);
        return "redirect:/";
    }

    //로그인
    @GetMapping("/login")
    public String loginUser(Model model){
        //model.addAttribute("loginDto",new loginDto());
        return "login";
    }
}
