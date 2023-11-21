package com.jobstore.jobstore.Controller.work;


import com.jobstore.jobstore.dto.request.worksContents.ContentsCheckedDto;
import com.jobstore.jobstore.dto.request.worksContents.ContentsCreateDto;
import com.jobstore.jobstore.dto.request.worksContents.ContentsUpdateDto;
import com.jobstore.jobstore.dto.response.ResultDto;
import com.jobstore.jobstore.service.work.ContentsService;
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
@Tag(name = "Work Content", description = "Work Content CRUD")
@RequestMapping("/work/content")
public class ContentController {

    @Autowired
    ContentsService contentService;

    @PostMapping("/create")
    @Operation(summary = "work 게시판 content todo 등록", description = "work 게시판 todo 등록.")
    @ResponseBody
    public ResponseEntity<ResultDto<ContentsCreateDto>> createWorkContent (
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = ContentsCreateDto.class)))
            @Valid @RequestBody ContentsCreateDto contentCreateDto
    ) {

        String result = contentService.createContent(contentCreateDto);
        if (result.equals("success")) {
            return ResponseEntity.ok(ResultDto.of("200", "등록 완료", contentCreateDto));
        } else if (result.equals("autherror")) {
            return ResponseEntity.ok(ResultDto.of("403", "등록 되지 않은 아이디", null));
        } else {
            return ResponseEntity.ok(ResultDto.of("500", "back error", null));
        }
    }


    @PatchMapping("/update")
    @Operation(summary = "work 게시판 content todo 수정", description = "work 게시판 todo 수정.")
    @ResponseBody
    public ResponseEntity<ResultDto<ContentsUpdateDto>> updateContent (
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = ContentsUpdateDto.class)))
            @Valid @RequestBody ContentsUpdateDto contentsUpdateDto
    ) {

        String result = contentService.updateContent(contentsUpdateDto);
        if (result.equals("success")) {
            return ResponseEntity.ok(ResultDto.of("200", "수정 완료", contentsUpdateDto));
        } else if (result.equals("nodata")) {
            return ResponseEntity.ok(ResultDto.of("403", "등록 되지 않은 컨텐츠", null));
        } else {
            return ResponseEntity.ok(ResultDto.of("500", "back error", null));
        }
    }


    @DeleteMapping ("/delete/{contensid}")
    @Operation(summary = "work 게시판 content todo 삭제", description = "work 게시판 todo 삭제.")
    @Parameter(name = "contensid", description = "id", required = true)
    @ResponseBody
    public ResponseEntity<ResultDto<ContentsUpdateDto>> deleteContent (
        @PathVariable("contensid") long contensid
    ) {

        int result = contentService.deleteContent(contensid);

        if (result == 1) {
            return ResponseEntity.ok(ResultDto.of("200", "삭제 완료", null));
        } else if (result == 0) {
            return ResponseEntity.ok(ResultDto.of("100", "삭제 실패.", null));
        } else if (result == 3){
            return ResponseEntity.ok(ResultDto.of("403", "없는 컨텐츠.", null));
        } else {
            return ResponseEntity.ok(ResultDto.of("500", "back error", null));
        }
    }


    @PatchMapping("/checked")
    @Operation(summary = "work 게시판 content todo checked", description = "work 게시판 todo checked.")
    @ResponseBody
    public ResponseEntity<ResultDto<ContentsCheckedDto>> contentChecked (
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = ContentsCheckedDto.class)))
            @Valid @RequestBody ContentsCheckedDto contentsCheckedDto
    ) {

        String result = contentService.checkedContent(contentsCheckedDto);
        if (result.equals("success")) {
            return ResponseEntity.ok(ResultDto.of("200", "등록 완료", contentsCheckedDto));
        } else if (result.equals("noMember")) {
            return ResponseEntity.ok(ResultDto.of("403", "등록 되지 않은 아이디", null));
        } else if (result.equals("nodata")) {
            return ResponseEntity.ok(ResultDto.of("403", "등록 되지 않은 contents", null));
        } else {
            return ResponseEntity.ok(ResultDto.of("500", "back error", null));
        }
    }

}
