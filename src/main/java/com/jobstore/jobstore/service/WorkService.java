package com.jobstore.jobstore.service;

import com.jobstore.jobstore.dto.request.work.WorkCreateDto;
import com.jobstore.jobstore.dto.request.work.WorkUpdateDto;
import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.entity.Store;
import com.jobstore.jobstore.entity.Work;
import com.jobstore.jobstore.repository.MemberRepository;
import com.jobstore.jobstore.repository.WorkRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@Service
public class WorkService {

    @Autowired
    private WorkRepository workRepository;
    @Autowired
    private MemberRepository memberRepository;

    // 페이지 네이션
    public List<Work> findPagenation (long storeid, long id) {

        List<Work> work =  workRepository.findTop10ByStoreidAndOrderByWorkid(storeid,id);
        System.out.println("work" + work);
        return  work;
    }

    public boolean validation (String meberid, long storeid) {

        Member existingMember;
        try {
            existingMember = memberRepository.findByMemberid(meberid)
                    .orElseThrow(() -> new RuntimeException("해당 멤버아이디는 존재하지 않는 멤버 아이디입니다"));
        } catch (Exception e) {
            return false;
        }
        // storeid 가 다르면
        if (existingMember.getStore().getStoreid() != Long.valueOf(storeid)) {
            return false;
        }
        return true;
    }


    public String createWorkBoard (WorkCreateDto workCreateDto) {

        System.out.println(workCreateDto.getStoreid());
//        try {
//            this.validation(workCreateDto.getMeberid(), workCreateDto.getStoreid());
//        } catch (Exception e) {
//            return "";
//        }
//
        Work work = new Work();
        work.setStoreid(1);
        work.setTitle(workCreateDto.getTitle());
        work.setDate(workCreateDto.getDate());
        workRepository.save(work);
        return  "";
    }

    public String updateWorkBoard (WorkUpdateDto workUpdateDto) {

        Optional<Work> work = workRepository.findByWorkid(workUpdateDto.getWorkid());

        if (!work.isPresent()) {
            return "없습니다.";
        }
        work.get().setTitle(workUpdateDto.getTitle());
        workRepository.save(work.get());
        return  "";
    }

    public String deleteWorkBoard (long id) {

        Optional<Work> work = workRepository.findByWorkid(id);
        if (!work.isPresent()) {
            return "없습니다.";
        }
        int result = workRepository.deletebyWorkid(id);

        if (result == 1) return "성공";
        else return  "실패";
    }





}
