package com.jobstore.jobstore.Controller;

import com.jobstore.jobstore.dto.RequestDto;
import com.jobstore.jobstore.dto.StoreDto;
import com.jobstore.jobstore.service.InvitationCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Tag(name = "InvitationCode", description = "Invitation code Generator")
public class InvitationCodeController {

        @Autowired
        private InvitationCodeService invitationCodeService;



        @PostMapping("/generate")
        @Operation(summary = "초대코드", description = "Company Number 일치 여부에 따라서 초대 코드가 반환 됩니다.")
        @ResponseBody
        public ResponseEntity<String> generateInvitationCode(
                @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                        content = @Content(
                                schema=@Schema(implementation = RequestDto.class)))
                @RequestBody StoreDto companynumber) {
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

