package com.jobstore.jobstore.dto.request.work;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkCreateDto {

    private String memberid;
    private Long storeid;
    private String title;
    private String date;

}
