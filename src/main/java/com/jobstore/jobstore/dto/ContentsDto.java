package com.jobstore.jobstore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContentsDto {
    private long contentsid;
    private String contents;
    private String checked;
}
