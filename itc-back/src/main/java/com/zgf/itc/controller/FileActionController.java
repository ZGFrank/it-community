package com.zgf.itc.controller;

import com.zgf.itc.utils.FileService;
import com.zgf.itc.utils.ResponseResult;
import com.zgf.itc.utils.TokenUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author ZGF
 */

@RestController
public class FileActionController {

    @Resource
    private TokenUtil tokenUtil;
    @Resource
    private FileService fileService;

    @RequestMapping("/upload")
    public ResponseResult fileUpload(@RequestParam("file") MultipartFile file,
                                     @RequestParam(name = "old",required = false) String old,
                                     HttpServletRequest request) {

        if (!tokenUtil.isExist(request)) {
            return ResponseResult.error("验证失败");
        }
        if (!StringUtils.isEmpty(old)) {
            fileService.delete(old);
        }
        return fileService.upload(file);
    }



}
