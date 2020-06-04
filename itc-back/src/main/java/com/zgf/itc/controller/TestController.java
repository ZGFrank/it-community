package com.zgf.itc.controller;

import com.zgf.itc.utils.FileService;
import com.zgf.itc.utils.ResponseResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author ZGF
 */
@RestController
@RequestMapping("test")
public class TestController {
    @Resource
    FileService service;

    @RequestMapping("image")
    public ResponseResult image(@RequestParam("file") MultipartFile file) {
        return this.service.upload(file);
    }
}
