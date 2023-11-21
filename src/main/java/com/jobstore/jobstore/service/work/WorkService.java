package com.jobstore.jobstore.service.work;

import com.jobstore.jobstore.dto.request.work.WorkCreateDto;
import com.jobstore.jobstore.dto.request.work.WorkUpdateDto;
import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.entity.Work;
import com.jobstore.jobstore.repository.MemberRepository;
import com.jobstore.jobstore.repository.work.WorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;


@Service
public class WorkService {

    @Autowired
    private WorkRepository workRepository;
    @Autowired
    private MemberRepository memberRepository;

    // 페이지 네이션
    public Page<Work> findPagenation (long storeid, Integer page){

        Integer size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Work> work =  workRepository.findByStoreid(storeid, pageRequest);
        HashMap<String, Object> hashMap = new HashMap<>();
        return work;

    }

    public Optional<Work> boardDetail (long workid) {

        Optional<Work> work = workRepository.findByWorkid(workid);

        if (work.isPresent()) {
            return work;
        }
        return null;
    }


    public String createWorkBoard (WorkCreateDto workCreateDto) {

        Optional<Member> member = memberRepository.findByMemberid(workCreateDto.getMemberid());
        System.out.println(member);
        if (!member.isPresent()) {
            return "noMember";
        }
        if (!member.get().getRole().equals("ADMIN")) {
            return "noAuth";
        }

        Work work = new Work();
        work.setStoreid(workCreateDto.getStoreid());
        work.setTitle(workCreateDto.getTitle());
        work.setDate(workCreateDto.getDate());
        workRepository.save(work);
        return "success";
    }

    public String updateWorkBoard (WorkUpdateDto workUpdateDto) {

        Optional<Work> work = workRepository.findByWorkid(workUpdateDto.getWorkid());
        if (!work.isPresent()) {
            return "nodata";
        }
        work.get().setTitle(workUpdateDto.getTitle());
        workRepository.save(work.get());
        return  "success";
    }

    public int deleteWorkBoard (long id) {

        Optional<Work> work = workRepository.findByWorkid(id);
        if (!work.isPresent()) {
            return 3;
        }
        int result = workRepository.deletebyWorkid(id);

        return result;
    }

}
