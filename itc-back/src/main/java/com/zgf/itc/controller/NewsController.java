package com.zgf.itc.controller;


import com.zgf.itc.entity.News;
import com.zgf.itc.service.NewsService;
import com.zgf.itc.utils.QueryCondition;
import com.zgf.itc.utils.ResponseResult;
import com.zgf.itc.vo.ShowData;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ZGF
 */
@RestController
@RequestMapping("/news")
public class NewsController {

    @Resource
    private NewsService service;

    @GetMapping("front")
    public ResponseResult getFrontNews() {
        try {
            List<News> list = this.service.getFrontNews();
            return ResponseResult.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @GetMapping("back")
    public ResponseResult getBackAll(QueryCondition qc) {
        try {
            ShowData data = this.service.getBackAll(qc);
            return ResponseResult.ok(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @PostMapping
    public ResponseResult insert(@RequestBody News entity) {
        try {
            this.service.insert(entity);
            return ResponseResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @PutMapping
    public ResponseResult update(@RequestBody News entity) {
        try {
            this.service.updateById(entity);
            return ResponseResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @DeleteMapping("{newsId}")
    public ResponseResult deleteById(@PathVariable("newsId") Integer newsId) {
        try {
            this.service.removeById(newsId);
            return ResponseResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @DeleteMapping
    public ResponseResult deleteBatch(@RequestParam("ids") String idsStr) {
        List<Integer> ids = Arrays.stream(idsStr.split(",")).map(Integer::valueOf).collect(Collectors.toList());
        try {
            this.service.removeByIds(ids);
            return ResponseResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }
}

