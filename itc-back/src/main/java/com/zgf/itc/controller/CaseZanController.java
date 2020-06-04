package com.zgf.itc.controller;


import com.zgf.itc.entity.CaseZan;
import com.zgf.itc.service.CaseZanService;
import com.zgf.itc.utils.ResponseResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ZGF
 */
@RestController
@RequestMapping("/caseZan")
public class CaseZanController {

    @Resource
    private CaseZanService service;

    @GetMapping("{yearSpace}/caseId/{uid}")
    public ResponseResult getCaseIdByUid(@PathVariable("yearSpace") Integer yearSpace, @PathVariable("uid") Integer uid) {
        try {
            List<Integer> cids = this.service.getCaseIdByUid(yearSpace, uid);
            return ResponseResult.ok(cids);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @PostMapping
    public ResponseResult saveZan(@RequestBody CaseZan entity) {
        try {
            if (this.service.saveZan(entity)) {
                return ResponseResult.ok();
            } else {
                return ResponseResult.error();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @DeleteMapping("{id}/user/{uid}")
    public ResponseResult deleteZan(@PathVariable("id") Integer id, @PathVariable("uid") Integer uid) {
        try {
            if (this.service.deleteZan(id,uid)) {
                return ResponseResult.ok();
            } else {
                return ResponseResult.error();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }
}

