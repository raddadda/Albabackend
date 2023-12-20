package com.jobstore.jobstore.dto.request.member;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageUploadDto {

    private String role;
    private String memberid;
    private long storeid;
}
