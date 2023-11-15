package com.jobstore.jobstore.Controller;

import com.jobstore.jobstore.dto.StoreDto;
import com.jobstore.jobstore.service.InvitationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class InvitationCodeController {

        @Autowired
        private InvitationCodeService invitationCodeService;

        @PostMapping("/generate")
        @ResponseBody
        public ResponseEntity<String> generateInvitationCode(@RequestBody StoreDto companynumber) {
            System.out.println(companynumber);
            String invitationCode = invitationCodeService.generateInvitationCode(companynumber);
            //companynumber와 일치하면 초대코드 발급 일치안하면 Invitation code not found
            //밑에 로직도 프론트랑 연동시 교체
            if (invitationCode != null) {
                return ResponseEntity.ok(invitationCode);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invitation code not found");
            }
        }
    }

