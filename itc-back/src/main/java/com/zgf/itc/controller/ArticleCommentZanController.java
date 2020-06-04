package com.zgf.itc.controller;


import com.zgf.itc.entity.ArticleCommentZan;
import com.zgf.itc.service.ArticleCommentZanService;
import com.zgf.itc.utils.ResponseResult;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/articleCommentZan")
public class ArticleCommentZanController {

    @Resource
    private ArticleCommentZanService service;

    /**
     * 当前当文章评论中当前用户点赞的数据
     *
     * @param artId
     * @param uId
     * @return
     */
    @GetMapping("{artId}/user/{uId}")
    public ResponseResult get(@PathVariable("artId") Integer artId, @PathVariable("uId") Integer uId) {
        List<Integer> isZan = this.service.currentArtComZanByUId(artId, uId);
        return ResponseResult.ok(isZan);
    }

    @PostMapping("save")
    public ResponseResult save(@RequestBody ArticleCommentZan zan) {
        try {
            if (this.service.saveArtComZan(zan)) {
                return ResponseResult.ok("点赞成功");
            }else{
                return ResponseResult.error("点赞失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }
}

