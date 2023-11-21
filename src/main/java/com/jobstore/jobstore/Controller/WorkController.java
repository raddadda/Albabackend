package com.jobstore.jobstore.Controller;


import com.jobstore.jobstore.dto.request.work.WorkCreateDto;
import com.jobstore.jobstore.dto.request.work.WorkUpdateDto;
import com.jobstore.jobstore.dto.response.ResultDto;
import com.jobstore.jobstore.entity.Work;
import com.jobstore.jobstore.service.WorkService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/work")
public class WorkController {

    @Autowired
    WorkService workService;
    @GetMapping("/boards/{storeid}/{id}")
    @ResponseBody
    public ResponseEntity<ResultDto<List<Work>>> findAllWorkBoard(@PathVariable("storeid") long storeid,
                                                                  @PathVariable("id") long id) {

        System.out.println(id);
        /** request
         *  // id
         *  // limit 10개
         * */

        /**
         *  Response
         *  pageSize
         *  List[]
         */

//        return ResponseEntity.ok(ResultDto.of("100","조회 완료",
//               ""));
        return ResponseEntity.ok(ResultDto.of("100","조회 완료",
                workService.findPagenation(storeid,id)));
    }


    @PostMapping("/board/create")
    @ResponseBody
    public ResponseEntity<ResultDto<String>> createWorkBoard (@Valid @RequestBody WorkCreateDto workCreateDto) {
        /**
         * storeid, memberid, title,date
         */

        System.out.println("여기");
        return ResponseEntity.ok(ResultDto.of("resultcode","등록 완료",
                    workService.createWorkBoard(workCreateDto)));
    }

    @PatchMapping("/board/update")
    @ResponseBody
    public ResponseEntity<ResultDto<String>> updateWorkBaord(@Valid @RequestBody WorkUpdateDto workUpdateDto) {

        /**
         * workid,title
         */
        return ResponseEntity.ok(ResultDto.of("resultcode","수정 완료",
                workService.updateWorkBoard(workUpdateDto)));
    }

    @DeleteMapping("/board/delete/{workid}")
    @ResponseBody
    public ResponseEntity<ResultDto<String>> deleteWorkBoard(@PathVariable("workid") long workid) {

        System.out.println("하하" + workid);
        return ResponseEntity.ok(ResultDto.of("resultcode","삭제 완료",
                workService.deleteWorkBoard(workid)));

    }
}
