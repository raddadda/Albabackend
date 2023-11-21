package com.jobstore.jobstore.Controller.work;


import com.jobstore.jobstore.dto.request.workComment.CommentCreateDto;
import com.jobstore.jobstore.dto.request.workComment.CommentUpdateDto;
import com.jobstore.jobstore.dto.request.worksContents.ContentsCreateDto;
import com.jobstore.jobstore.dto.response.ResultDto;
import com.jobstore.jobstore.service.work.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Work Commnet", description = "Work Commnet CRUD")
@RequestMapping("/work/commnet")
public class CommentController {

    @Autowired
    CommentService commentService;

    @PostMapping("/create")
    @Operation(summary = "work 게시판 댓글 등록", description = "work 게시판 댓글 등록.")
    @ResponseBody
    public ResponseEntity<ResultDto<CommentCreateDto>> createWorkContent (
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = ContentsCreateDto.class)))
            @Valid @RequestBody CommentCreateDto commentCreateDto
    ) {

        String result = commentService.createComment(commentCreateDto);
        if (result.equals("success")) {
            return ResponseEntity.ok(ResultDto.of("200", "등록 완료", commentCreateDto));
        } else if (result.equals("nodata")) {
            return ResponseEntity.ok(ResultDto.of("403", "등록 되지 work 게시판", null));
        } else if (result.equals("noMember")) {
            return ResponseEntity.ok(ResultDto.of("403", "등록 되지 않은 아이디", null));
        } else {
            return ResponseEntity.ok(ResultDto.of("500", "back error", null));
        }
    }


    @PatchMapping("/update")
    @Operation(summary = "work 게시판 content 댓글 수정", description = "work 게시판 댓글 수정.")
    @ResponseBody
    public ResponseEntity<ResultDto<CommentUpdateDto>> updateContent (
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = CommentUpdateDto.class)))
            @Valid @RequestBody CommentUpdateDto commentUpdateDto
    ) {

        String result = commentService.updateComment(commentUpdateDto);
        if (result.equals("success")) {
            return ResponseEntity.ok(ResultDto.of("200", "수정 완료", commentUpdateDto));
        } else if (result.equals("nodata")) {
            return ResponseEntity.ok(ResultDto.of("403", "등록 되지 않은 댓글", null));
        } else {
            return ResponseEntity.ok(ResultDto.of("500", "back error", null));
        }
    }


    @DeleteMapping ("/delete/{commentid}")
    @Operation(summary = "work 게시판 댓글  삭제", description = "work 게시판 댓글 삭제.")
    @Parameter(name = "commentid", description = "id", required = true)
    @ResponseBody
    public ResponseEntity<ResultDto<Object>> deleteContent (
            @PathVariable("commentid") long commentid
    ) {

        int result = commentService.deleteComment(commentid);

        if (result == 1) {
            return ResponseEntity.ok(ResultDto.of("200", "삭제 완료", null));
        } else if (result == 0) {
            return ResponseEntity.ok(ResultDto.of("100", "삭제 실패.", null));
        } else if (result == 3){
            return ResponseEntity.ok(ResultDto.of("403", "등록 되지 않은 댓글.", null));
        } else {
            return ResponseEntity.ok(ResultDto.of("500", "back error", null));
        }
    }
}
