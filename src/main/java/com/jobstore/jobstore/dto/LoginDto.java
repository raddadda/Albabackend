package com.jobstore.jobstore.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

    @Getter
    @Setter
    public class LoginDto {
        private String memberid;
        private String password;
        private String role;
    }
