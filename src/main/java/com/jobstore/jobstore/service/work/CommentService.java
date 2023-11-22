package com.jobstore.jobstore.service.work;


import com.jobstore.jobstore.dto.request.workComment.CommentCreateDto;
import com.jobstore.jobstore.dto.request.workComment.CommentUpdateDto;
import com.jobstore.jobstore.entity.Comment;
import com.jobstore.jobstore.entity.Member;
import com.jobstore.jobstore.entity.Work;
import com.jobstore.jobstore.repository.MemberRepository;
import com.jobstore.jobstore.repository.work.CommentsRepository;
import com.jobstore.jobstore.repository.work.WorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private WorkRepository workRepository;
    @Autowired
    private CommentsRepository commentsRepository;

    public String createComment (CommentCreateDto commentCreateDto) {

        Optional<Work> existingWork = workRepository.findByWorkid(commentCreateDto.getWorkid());
        if (!existingWork.isPresent()){
            return "nodata";
        }
        Optional<Member> member = memberRepository.findByMemberid(commentCreateDto.getMemberid());
        if (!member.isPresent()) {
            return "noMember";
        }
        Comment comment = new Comment();
        comment.setName(member.get().getName());
        comment.setComment(commentCreateDto.getComment());

        Member member1 = new Member();
        member1.setMemberid(commentCreateDto.getMemberid());
        Work work = new Work();
        work.setWorkid(commentCreateDto.getWorkid());
        comment.setWork(work);
        comment.setMember(member1);

        commentsRepository.save(comment);
        return "success";
    }

    public String updateComment (CommentUpdateDto commentUpdateDto) {

        Optional<Comment> comment = commentsRepository.findByCommentid(commentUpdateDto.getCommentid());
        if (!comment.isPresent()) {
            return "nodata";
        }
        comment.get().setComment(commentUpdateDto.getComment());
        commentsRepository.save(comment.get());
        return  "success";

    }

    public int deleteComment (long commentid) {

        Optional<Comment> Contents = commentsRepository.findByCommentid(commentid);

        if (!Contents.isPresent()) {
            return 3;
        }
        return commentsRepository.deletebyCommentid(commentid);

    }
}
