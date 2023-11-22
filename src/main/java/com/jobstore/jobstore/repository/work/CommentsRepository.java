package com.jobstore.jobstore.repository.work;

import com.jobstore.jobstore.entity.Comment;
import com.jobstore.jobstore.entity.Contents;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentsRepository extends JpaRepository<Comment,Long> {



    Optional<Comment> findByCommentid (long commentid);

    List<Comment> findByWorkWorkid(long workid, Sort sort);

    @Transactional
    @Modifying // 조회 X
    @Query("DELETE FROM Comment c WHERE c.commentid = :id")
    int deletebyCommentid(@Param("id") long id);

}
