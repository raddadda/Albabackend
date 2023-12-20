package com.jobstore.jobstore.controller.work;


import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<ResultDto<WorkPagenationDto>> findAllWorkBoard(@PathVariable(value = "storeid", required = true) long storeid,
                                                                         @PathVariable(value = "page" , required = true) Integer page) {

        try {
            return ResponseEntity.ok(ResultDto.of("200","조회 완료",
                    workService.findPagenation(storeid, page)));
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/boards/{storeid}/{search}/{page}")
    @Operation(summary = "work 게시판 검색어 페이지네이션", description = "work 게시판 검색어 페이지네이션입니다.")
    @Parameter(name = "storeid", description = "storeid", required = true)
    @Parameter(name = "search", description = "search", required = true)
    @Parameter(name = "page", description = "페이지 번호 0부터 시작", required = true)
    @ResponseBody
    public ResponseEntity<ResultDto<WorkPagenationDto>> searchWorkBoard(@PathVariable(value = "storeid", required = true) long storeid,
                                                                        @PathVariable(value = "search", required = true) String search,
                                                                         @PathVariable(value = "page" , required = true) Integer page) {

            try {
                return ResponseEntity.ok(ResultDto.of("200","조회 완료",
                        workService.searchPagenation(storeid, search, page)));
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
    }


    @GetMapping("/boards/detail/{workid}")
    @Operation(summary = "work 게시판 상세", description = "work 게시판 상세.")
    @Parameter(name = "workid", description = "workid", required = true)
    @ResponseBody
    public ResponseEntity<ResultDto<WorkDetailDto>> boardDetail(@PathVariable(value = "workid", required = true) long workid) {
        try {
            WorkDetailDto result = workService.boardDetail(workid);

            if (result == null) {
                return ResponseEntity.ok(ResultDto.of("403","게시판 없음",
                        null));
            } else if ((Long)result.getWorkid() != null) {
                return ResponseEntity.ok(ResultDto.of("200","조회 완료",
                        result));
            } return null;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }


    @PostMapping("/board/create")
    @Operation(summary = "work 게시판 등록", description = "work 게시판 등록입니다.")
    @ResponseBody
    public ResponseEntity<ResultDto<Object>> createWorkBoard (
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "요청파라미터", required = true,
                    content = @Content(schema=@Schema(implementation = WorkCreateDto.class)))
            @Valid @RequestBody WorkCreateDto workCreateDto
    ) {
        try {
            HashMap result = workService.createWorkBoard(workCreateDto);

            if (result.get("message").equals("success")) {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> map = objectMapper.convertValue(workCreateDto, Map.class);
                map.put("workid", result.get("workid"));
                return ResponseEntity.ok(ResultDto.of("200", "등록 완료", map));
            } else if ( result.get("message").equals("noMember") ) {
                return ResponseEntity.ok(ResultDto.of("403", "등록 되지 않은 아이디", null));
            } else if (result.get("message").equals("noAuth")) {
                return ResponseEntity.ok(ResultDto.of("401", "허용 되지 않는 권한", null));
            } return null;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
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
        try {
            String result = workService.updateWorkBoard(workUpdateDto);

            if (result.equals("success")) {
                return ResponseEntity.ok(ResultDto.of("200", "수정 완료", workUpdateDto));
            } else if (result.equals("nodata")) {
                return ResponseEntity.ok(ResultDto.of("403", "없는 게시물입니다.", null));
            } return null;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }

    @DeleteMapping("/board/delete/{workid}")
    @Operation(summary = "work 게시판 삭제", description = "work 게시판 삭제입니다.")
    @Parameter(name = "workid", description = "workid", required = true)
    @ResponseBody
    public ResponseEntity<ResultDto<Object>> deleteWorkBoard(@PathVariable("workid") int workid) {

       try {
           int result = workService.deleteWorkBoard(workid);
           if (result == 1) {
               return ResponseEntity.ok(ResultDto.of("200", "삭제 완료", null));
           } else if (result == 0) {
               return ResponseEntity.ok(ResultDto.of("100", "삭제 실패.", null));
           } else if (result == 3){
               return ResponseEntity.ok(ResultDto.of("403", "없는 게시판.", null));
           }return null;
       }catch (Exception e){
           throw new RuntimeException(e.getMessage());
       }
    }
}
