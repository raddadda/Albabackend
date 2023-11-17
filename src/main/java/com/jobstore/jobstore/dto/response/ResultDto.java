package com.jobstore.jobstore.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor(staticName = "of")
public class ResultDto<D> {
    private final String resultCode;
    private final String message;
    private final D data;

}
