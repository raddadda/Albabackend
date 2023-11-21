package com.jobstore.jobstore.dto.request.workComment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateDto {

    private String memberid;
    private long commentid;
    private String comment;
    private String name;

}
