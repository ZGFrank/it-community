package com.zgf.itc.controller;


import com.zgf.itc.entity.Category;
import com.zgf.itc.service.CategoryService;
import com.zgf.itc.utils.ResponseResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ZGF
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService service;

    @GetMapping("{type}/loads/{available}")
    public ResponseResult loads(@PathVariable("type") String type, @PathVariable("available") String available) {
        List<Category> categories = null;
        try {
            if ("back".equals(type)) {
                categories = service.loads(0, available);
            } else {
                categories = service.loads(1, available);
            }
            return ResponseResult.ok("获取成功", categories);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("服务器错误");
        }

    }

    @GetMapping
    public ResponseResult loads() {
        List<Category> list = this.service.list();
        return ResponseResult.ok(list);
    }

    @GetMapping("front")
    public ResponseResult loadForFront() {
        try {
            List<Category> list = this.service.loadForFront();
            return ResponseResult.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @PostMapping
    public ResponseResult save(@RequestBody Category entity) {
        try {
            if (this.service.saveCate(entity)) {
                return ResponseResult.ok();
            } else {
                return ResponseResult.error();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @PutMapping
    public ResponseResult update(@RequestBody Category entity) {
        try {
            if (this.service.updateCate(entity)) {
                return ResponseResult.ok();
            } else {
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
            } else {
                return ResponseResult.error();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @DeleteMapping
    public ResponseResult deleteBatch(Integer[] ids) {
        try {
            if (this.service.removeByIds(Arrays.asList(ids))) {
                return ResponseResult.ok();
            } else {
                return ResponseResult.error();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }
}

