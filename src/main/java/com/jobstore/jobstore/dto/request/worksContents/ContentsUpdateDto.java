package com.jobstore.jobstore.dto.request.worksContents;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContentsUpdateDto {

    private long contentsid;
    private String contents;
}
