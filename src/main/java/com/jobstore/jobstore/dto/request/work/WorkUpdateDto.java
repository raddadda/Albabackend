package com.jobstore.jobstore.dto.request.work;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkUpdateDto {

    private long workid;
    private String title;

}
