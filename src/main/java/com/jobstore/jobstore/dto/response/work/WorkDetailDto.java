package com.jobstore.jobstore.dto.response.work;


import com.jobstore.jobstore.entity.Comment;
import com.jobstore.jobstore.entity.Contents;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkDetailDto<T> {
    private long workid;
    private String title;
    private String date;
    private List<Contents> contents;
    private Collection<Comment> comment;
}
