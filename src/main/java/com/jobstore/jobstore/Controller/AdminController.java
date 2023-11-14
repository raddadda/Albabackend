package com.jobstore.jobstore.Controller;

import com.jobstore.jobstore.dto.AdminDto;
import com.jobstore.jobstore.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {

    @Autowired AdminService adminService;
    //회원가입
    @GetMapping("/admin/join")
    public String joinAdmin(Model model){
        model.addAttribute("adminDto",new AdminDto());
        return "adminJoin";
    }
    @PostMapping("/admin/join")
    public String createAdmin(AdminDto adminDto){
        //adminService.join(adminDto);
        return "adminJoin";
    }

    //로그인
    @GetMapping("/admin/login")
    public String loginAdmin(Model model){
       // model.addAttribute("loginDto",new loginDto());
        return "adminJoin";
    }

}
