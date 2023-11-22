package com.jobstore.jobstore.dto.request.worksContents;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContentsCreateDto {

    private long workid;
    private String memberid;
    private String contents;

}
