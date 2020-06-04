package com.zgf.itc.controller;


import com.zgf.itc.entity.Case;
import com.zgf.itc.service.CaseService;
import com.zgf.itc.utils.QueryCondition;
import com.zgf.itc.utils.ResponseResult;
import com.zgf.itc.vo.ShowData;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ZGF
 */
@RestController
@RequestMapping("/case")
public class CaseController {

    @Resource
    private CaseService service;

    @PostMapping
    public ResponseResult save(@RequestBody Case entity) {
        try {
            if (this.service.saveCase(entity)) {
                return ResponseResult.ok();
            }else {
                return ResponseResult.error();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @GetMapping
    public ResponseResult getAll(QueryCondition query) {
        try {
            ShowData data = this.service.getAll(query);
            return ResponseResult.ok(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @GetMapping("back")
    public ResponseResult getBack(QueryCondition query) {
        try {
            ShowData data = this.service.getBack(query);
            return ResponseResult.ok(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @PutMapping
    public ResponseResult update(@RequestBody Case entity) {
        try {
            if (this.service.updateCase(entity)) {
                return ResponseResult.ok();
            }else {
                return ResponseResult.error();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @DeleteMapping("{id}")
    public ResponseResult delete(@PathVariable("id") Integer id) {
        try {
            if (this.service.removeById(id)) {
                return ResponseResult.ok();
            }else {
                return ResponseResult.error();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }
}

