package com.jobstore.jobstore.controller.work;


import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.HashMap;
import java.util.Map;

@RestController
@Tag(name = "Work Comment", description = "Work Comment CRUD")
@RequestMapping("/work/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @PostMapping("/create")
    @Operation(summary = "work 게시판 댓글 등록", description = "work 게시판 댓글 등록.")
    @ResponseBody
    public ResponseEntity<ResultDto<Object>> createWorkContent (
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation =CommentCreateDto.class)))
            @Valid @RequestBody CommentCreateDto commentCreateDto
    ) {

        try {
            HashMap result = commentService.createComment(commentCreateDto);
            if (result.get("message").equals("success")) {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> map = objectMapper.convertValue(commentCreateDto, Map.class);
                map.put("commentid", result.get("commentid"));
                return ResponseEntity.ok(ResultDto.of("200", "등록 완료", map));
            } else if (result.get("message").equals("nodata")) {
                return ResponseEntity.ok(ResultDto.of("403", "등록 되지 work 게시판", null));
            } else if (result.get("message").equals("noMember")) {
                return ResponseEntity.ok(ResultDto.of("403", "등록 되지 않은 아이디", null));
            }
            return null;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
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
        try {
            String result = commentService.updateComment(commentUpdateDto);
            if (result.equals("success")) {
                return ResponseEntity.ok(ResultDto.of("200", "수정 완료", commentUpdateDto));
            } else if (result.equals("nodata")) {
                return ResponseEntity.ok(ResultDto.of("403", "등록 되지 않은 댓글", null));
            }
            return null;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }


    @DeleteMapping ("/delete/{commentid}")
    @Operation(summary = "work 게시판 댓글  삭제", description = "work 게시판 댓글 삭제.")
    @Parameter(name = "commentid", description = "commentid", required = true)
    @ResponseBody
    public ResponseEntity<ResultDto<Object>> deleteContent (
            @PathVariable("commentid") int commentid
    ) {
        try {
            int result = commentService.deleteComment(commentid);

            if (result == 1) {
                return ResponseEntity.ok(ResultDto.of("200", "삭제 완료", null));
            } else if (result == 0) {
                return ResponseEntity.ok(ResultDto.of("100", "삭제 실패.", null));
            } else if (result == 3){
                return ResponseEntity.ok(ResultDto.of("403", "등록 되지 않은 댓글.", null));
            } return null;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
