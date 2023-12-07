package com.jobstore.jobstore.dto.request.auth;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenValidationDto {
    private String token;
}
