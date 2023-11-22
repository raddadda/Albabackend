package com.jobstore.jobstore.service.work;


import com.jobstore.jobstore.dto.request.worksContents.ContentsCheckedDto;
import com.jobstore.jobstore.dto.request.worksContents.ContentsCreateDto;
import com.jobstore.jobstore.dto.request.worksContents.ContentsUpdateDto;
import com.jobstore.jobstore.entity.Contents;
import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.entity.Work;
import com.jobstore.jobstore.repository.MemberRepository;
import com.jobstore.jobstore.repository.work.ContentRepository;
import com.jobstore.jobstore.repository.work.WorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContentsService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private WorkRepository workRepository;
    @Autowired
    private ContentRepository contentsRepository;


    public String createContent (ContentsCreateDto contentCreateDto) {

        Optional<Work> existingWork = workRepository.findByWorkid(contentCreateDto.getWorkid());
        if (!existingWork.isPresent()){
            return "nodata";
        }

        Contents contents = new Contents();
        contents.setContents(contentCreateDto.getContents());
        Work work = new Work();
        work.setWorkid(contentCreateDto.getWorkid());
        contents.setWork(work);
        contentsRepository.save(contents);
        return "success";
    }

    public String updateContent (ContentsUpdateDto contentsUpdateDto) {

        Optional<Contents> contents = contentsRepository.findByContentsid(contentsUpdateDto.getContentsid());
        if (!contents.isPresent()) {
            return "nodata";
        }

        contents.get().setContentsid(contentsUpdateDto.getContentsid());
        contents.get().setContents(contentsUpdateDto.getContents());
        contentsRepository.save(contents.get());
        return  "success";

    }

    public int deleteContent (long contentsid) {

        Optional<Contents> Contents = contentsRepository.findByContentsid(contentsid);

        if (!Contents.isPresent()) {
            return 3;
        }
        return contentsRepository.deletebyContentsid(contentsid);

    }

    public String checkedContent (ContentsCheckedDto contentsCheckedDto) {

        Optional<Member> member = memberRepository.findByMemberid(contentsCheckedDto.getMemberid());
        if (!member.isPresent()) {
            return "noMember";
        }

        Optional<Contents> contents = contentsRepository.findByContentsid(contentsCheckedDto.getContentsid());
        if (!contents.isPresent()) {
            return "nodata";
        }
        contents.get().setChecked(contentsCheckedDto.getChecked());
        contentsRepository.save(contents.get());
        return  "success";
    }

}
