package com.zgf.itc.controller;


import com.zgf.itc.entity.Report;
import com.zgf.itc.service.ReportService;
import com.zgf.itc.utils.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ZGF
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Resource
    private ReportService service;

    @PostMapping
    public ResponseResult save(@RequestBody Report entity) {
        try {
            if (this.service.saveReport(entity)) {
                return ResponseResult.ok();
            }
            return ResponseResult.error();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }
}

