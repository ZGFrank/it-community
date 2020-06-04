package com.zgf.itc.controller;


import com.zgf.itc.entity.ArticleComment;
import com.zgf.itc.service.ArticleCommentService;
import com.zgf.itc.utils.ResponseResult;
import com.zgf.itc.vo.ArtComTreeNode;
import com.zgf.itc.vo.RecentComment;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 文章评论表 前端控制器
 * </p>
 *
 * @author ZGF
 */
@RestController
@RequestMapping("/articleComment")
public class ArticleCommentController {

    @Resource
    private ArticleCommentService service;

    /**
     * 通过文字ID获取文章评论
     * @param artId
     * @return
     */
    @GetMapping("{artId}")
    public ResponseResult getArtCommentByArtId(@PathVariable("artId") Integer artId) {
        try {
            List<ArtComTreeNode> tree = this.service.getComments(artId);
            return ResponseResult.ok(tree);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    /**
     * 获取用户最近评论
     * @param uid
     * @return
     */
    @GetMapping("recent/{uid}")
    public ResponseResult getRecentComment(@PathVariable("uid") Integer uid) {
        try {
            List<RecentComment> recent = this.service.getRecentComment(uid, 10);
            return ResponseResult.ok(recent);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    /**
     * 保存用户评论
     * @param comment ArticleComment类型的评论对象
     * @return 结果对象
     */
    @PostMapping("save")
    public ResponseResult save(@RequestBody ArticleComment comment) {
        if (this.service.saveComment(comment)) {
            return ResponseResult.ok();
        } else {
            return ResponseResult.e500();
        }

    }

    /**
     * 更新用户评论
     * @param comment
     * @return
     */
    @PutMapping("update")
    public ResponseResult update(@RequestBody ArticleComment comment) {
        if (this.service.updateById(comment)) {
            return ResponseResult.ok();
        } else {
            return ResponseResult.error();
        }
    }

    /**
     * 删除用户评论
     * @param artCId
     * @param msg
     * @return
     */
    @DeleteMapping("{artCId}")
    public ResponseResult delete(@PathVariable("artCId") Integer artCId
            ,@RequestParam(name = "msg",required = false) String msg) {
        if (this.service.deleteById(artCId,msg)) {
            return ResponseResult.ok();
        } else {
            return ResponseResult.error();
        }
    }

    /**
     * 采纳评论
     *
     * @param artCId 采纳评论的ID
     * @param cUid   评论的用户ID
     * @param artId  评论多的文章ID
     * @param uId    文章所属用户
     * @return
     */
    @PutMapping("{artCId}/take/{cUid}/art/{artId}/user/{uId}")
    public ResponseResult take(@PathVariable("artCId") Integer artCId
            , @PathVariable("cUid") Integer cUid
            , @PathVariable("artId") Integer artId
            , @PathVariable("uId") Integer uId) {

        if (this.service.take(artCId, cUid, artId, uId)) {
            return ResponseResult.ok();
        } else {
            return ResponseResult.e500();
        }
    }

}

