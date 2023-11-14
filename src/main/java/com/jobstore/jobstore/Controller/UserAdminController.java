package com.jobstore.jobstore.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserAdminController{

//    @Autowired
//    private UserAdminService userAdminService;

    //home화면
    @GetMapping("/")
    public String home(Model model){
        return "home";
    }

//    //회원가입
//    @GetMapping("/join")
//    public String joinForm(Model model){
//        model.addAttribute("useradminForm",new useradminForm());
//    }
//    //로그인
}
