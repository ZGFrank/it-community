package com.zgf.itc.controller;


import com.zgf.itc.service.SignInService;
import com.zgf.itc.utils.ResponseResult;
import com.zgf.itc.vo.SignInBaseInfo;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ZGF
 */
@RestController
@RequestMapping("/sign")
public class SignInController {

    @Resource
    private SignInService service;
    @GetMapping("info/{uid}")
    public ResponseResult getSignInfo(@PathVariable("uid") Integer uid) {
        try {
            SignInBaseInfo info = this.service.getSignInfo(uid);
            return ResponseResult.ok(info);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @PostMapping("{uid}")
    public ResponseResult save(@PathVariable("uid") Integer uid) {
        try {
            if (this.service.saveRecord(uid)) {
                return ResponseResult.ok();
            }else {
                return ResponseResult.e404();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }
}

