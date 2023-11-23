package com.jobstore.jobstore.dto;

import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberAndStoreDetailsDto {
    private Member member;
    private Store store;
}
