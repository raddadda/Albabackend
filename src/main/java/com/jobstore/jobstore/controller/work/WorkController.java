package com.jobstore.jobstore.controller.work;


import com.jobstore.jobstore.dto.request.work.WorkCreateDto;
import com.jobstore.jobstore.dto.request.work.WorkUpdateDto;
import com.jobstore.jobstore.dto.response.ResultDto;
import com.jobstore.jobstore.dto.response.work.WorkDetailDto;
import com.jobstore.jobstore.dto.response.work.WorkPagenationDto;
import com.jobstore.jobstore.service.work.WorkService;
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
@Tag(name = "Work", description = "Work CRUD")
@RequestMapping("/work")
public class WorkController {



    @Autowired
    WorkService workService;
    @GetMapping("/boards/{storeid}/{page}")
    @Operation(summary = "work 게시판 페이지네이션", description = "work 게시판 페이지네이션입니다.")
    @Parameter(name = "storeid", description = "storeid", required = true)
    @Parameter(name = "page", description = "페이지 번호 0부터 시작", required = true)
    @ResponseBody
    public ResponseEntity<ResultDto<WorkPagenationDto>> findAllWorkBoard(@PathVariable("storeid") long storeid,
                                                                         @PathVariable("page") Integer page) {
        return ResponseEntity.ok(ResultDto.of("100","조회 완료",
                workService.findPagenation(storeid, page)));
    }

    @GetMapping("/boards/detail/{workid}")
    @Operation(summary = "work 게시판 상세", description = "work 게시판 상세.")
    @Parameter(name = "workid", description = "workid", required = true)
    @ResponseBody
    public ResponseEntity<ResultDto<WorkDetailDto>> boardDetail(@PathVariable("workid") long workid) {

        WorkDetailDto result = workService.boardDetail(workid);

        if (result == null) {
            return ResponseEntity.ok(ResultDto.of("403","게시판 없음",
                    null));
        } else if ((Long)result.getWorkid() != null) {
            return ResponseEntity.ok(ResultDto.of("200","조회 완료",
                    result));
        } else {
            return ResponseEntity.ok(ResultDto.of("500","back error",
                    null));
        }
    }

    @PostMapping("/board/create")
    @Operation(summary = "work 게시판 등록", description = "work 게시판 등록입니다.")
    @ResponseBody
    public ResponseEntity<ResultDto<WorkCreateDto>> createWorkBoard (
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = WorkCreateDto.class)))
            @Valid @RequestBody WorkCreateDto workCreateDto
    ) {

        String result = workService.createWorkBoard(workCreateDto);

        if (result.equals("success")) {
            return ResponseEntity.ok(ResultDto.of("200", "등록 완료", workCreateDto));
        } else if ( result.equals("noMember") ) {
            return ResponseEntity.ok(ResultDto.of("403", "등록 되지 않은 아이디", null));
        } else if (result.equals("noAuth")) {
            return ResponseEntity.ok(ResultDto.of("401", "허용 되지 않는 권한", null));
        } else {
            return ResponseEntity.ok(ResultDto.of("500", "back error", null));
        }
    }

    @PatchMapping("/board/update")
    @Operation(summary = "work 게시판 수정", description = "work 게시판 수정입니다.")
    @ResponseBody
    public ResponseEntity<ResultDto<Object>> updateWorkBaord(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = WorkUpdateDto.class)))
            @Valid @RequestBody WorkUpdateDto workUpdateDto
    ) {

        String result = workService.updateWorkBoard(workUpdateDto);

        if (result.equals("success")) {
            return ResponseEntity.ok(ResultDto.of("200", "수정 완료", workUpdateDto));
        } else if (result.equals("nodata")) {
            return ResponseEntity.ok(ResultDto.of("403", "없는 게시물입니다.", null));
        } else {
            return ResponseEntity.ok(ResultDto.of("500", "back error", null));
        }
    }

    @DeleteMapping("/board/delete/{workid}")
    @Operation(summary = "work 게시판 삭제", description = "work 게시판 삭제입니다.")
    @Parameter(name = "workid", description = "workid", required = true)
    @ResponseBody
    public ResponseEntity<ResultDto<Object>> deleteWorkBoard(@PathVariable("workid") long workid) {

        int result = workService.deleteWorkBoard(workid);
        if (result == 1) {
            return ResponseEntity.ok(ResultDto.of("200", "삭제 완료", null));
        } else if (result == 0) {
            return ResponseEntity.ok(ResultDto.of("100", "삭제 실패.", null));
        } else if (result == 3){
            return ResponseEntity.ok(ResultDto.of("403", "없는 게시판.", null));
        }else {
            return ResponseEntity.ok(ResultDto.of("500", "back error", null));
        }

    }
}
