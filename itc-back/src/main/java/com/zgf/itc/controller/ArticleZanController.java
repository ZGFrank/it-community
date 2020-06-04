package com.zgf.itc.controller;


import com.zgf.itc.entity.ArticleZan;
import com.zgf.itc.service.ArticleZanService;
import com.zgf.itc.utils.ResponseResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ZGF
 */
@RestController
@RequestMapping("/articleZan")
public class ArticleZanController {

    @Resource
    private ArticleZanService service;

    /**
     * 当前用户是否赞过当前当文章
     * ram artId
     *
     * @param uId
     * @return
     */
    @GetMapping("{artId}/user/{uId}")
    public ResponseResult get(@PathVariable("artId") Integer artId, @PathVariable("uId") Integer uId) {
        boolean isZan = this.service.currentArtZanByUId(artId, uId);
        Map<String, Boolean> map = new HashMap<>(1);
        map.put("zan", isZan);
        return ResponseResult.ok(map);
    }

    @PostMapping("save")
    public ResponseResult save(@RequestBody ArticleZan zan) {
        if (this.service.saveArticleZan(zan)) {
            return ResponseResult.ok("点赞成功");
        } else {
            return ResponseResult.error("点赞失败");
        }
    }
}

