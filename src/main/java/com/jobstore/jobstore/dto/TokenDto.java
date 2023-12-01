package com.jobstore.jobstore.dto;

import com.jobstore.jobstore.dto.request.member.DoubleCheckDto;
import com.jobstore.jobstore.entity.Attendance;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    private String memberid;
    private Long storeid;
    private String token;
}
