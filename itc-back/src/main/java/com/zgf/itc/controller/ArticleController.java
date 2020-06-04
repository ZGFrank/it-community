package com.zgf.itc.controller;


import com.zgf.itc.entity.Article;
import com.zgf.itc.service.ArticleService;
import com.zgf.itc.service.impl.ArticleServiceImpl;
import com.zgf.itc.utils.Pagination;
import com.zgf.itc.utils.QueryCondition;
import com.zgf.itc.utils.ResponseResult;
import com.zgf.itc.utils.TokenUtil;
import com.zgf.itc.vo.ArticleDetail;
import com.zgf.itc.vo.ArticleItem;
import com.zgf.itc.vo.ShowData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ZGF
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService service;
    @Resource
    private TokenUtil tokenUtil;

    @PostMapping("save")
    public ResponseResult save(@RequestBody Article article, HttpServletRequest request) {
        if (!tokenUtil.isExist(request)) {
            return ResponseResult.error("验证失败，请重新登录");
        }
        boolean isSave = this.service.saveArticle(article);
        if (isSave) {
            return ResponseResult.ok("添加成功");
        }
        return ResponseResult.error("添加失败");
    }

    @GetMapping("loadsTop")
    public ResponseResult loadsTop() {
        try {
            List<ArticleItem> list = this.service.loadsTop();
            return ResponseResult.ok("获取成功", list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("服务器错误");

        }
    }

    @GetMapping("loadsNormal")
    public ResponseResult loadsNormal(QueryCondition qc,HttpServletRequest request) {
        try {
            Integer uid = null;
            if (tokenUtil.isExist(request)) {
                uid = tokenUtil.getUidByToken(request);
            }
            ShowData data = this.service.loadsNormal(qc,uid);
            return ResponseResult.ok("获取成功", data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();

        }
    }

    @GetMapping("back")
    public ResponseResult loadForBack(QueryCondition qc) {
        try {
            ShowData data = this.service.loadForBack(qc);
            return ResponseResult.ok("获取成功", data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("服务器错误");

        }
    }

    @GetMapping("{artId}/user/{uId}")
    public ResponseResult getArticleById(@PathVariable("artId") Integer artId, @PathVariable("uId") Integer uId) {

        try {
            ArticleDetail article = this.service.getArticleById(artId, uId);
            if (article != null) {
                return ResponseResult.ok(article);
            } else {
                return ResponseResult.e404();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @GetMapping("all/{uId}")
    public ResponseResult getArticles(@PathVariable("uId") Integer uId, Pagination pagination) {
        try {
            ShowData articles = this.service.getArticlesMyUid(uId, pagination);
            return ResponseResult.ok(articles);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @GetMapping("recent/{uid}")
    public ResponseResult getArticles(@PathVariable("uid") Integer uid) {
        try {
            List<Article> recent = this.service.getRecentArticle(uid, 10);
            return ResponseResult.ok(recent);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @GetMapping("weekTop")
    public ResponseResult getWeekTopArticle() {
        try {
            List<Article> list = this.service.getWeekTopArticle();
            return ResponseResult.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @PutMapping("update")
    public ResponseResult update(@RequestBody Article article, HttpServletRequest request) {

        if (!tokenUtil.isExist(request)) {
            return ResponseResult.error("验证失败，请重新登录");
        }
        if (this.service.updateArticle(article)) {
            return ResponseResult.ok();
        } else {
            return ResponseResult.error();
        }
    }

    @PutMapping("top")
    public ResponseResult updateTop(@RequestBody Article article, HttpServletRequest request) {

        if (!tokenUtil.isExist(request)) {
            return ResponseResult.error("验证失败，请重新登录");
        }
        if (this.service.updateTop(article)) {
            return ResponseResult.ok();
        } else {
            return ResponseResult.error();
        }
    }

    @DeleteMapping("{artId}")
    public ResponseResult delete(@PathVariable("artId") Integer artId
            , @RequestParam("msg") String msg
            , HttpServletRequest request) {

        if (!tokenUtil.isExist(request)) {
            return ResponseResult.error("验证失败，请重新登录");
        }
        if (this.service.deleteArticle(artId,msg)) {
            return ResponseResult.ok();
        } else {
            return ResponseResult.error();
        }
    }

    @GetMapping("search")
    public ResponseResult search(QueryCondition qc) {
        ShowData data = this.service.search(qc);
        return ResponseResult.ok(data);
    }

}

