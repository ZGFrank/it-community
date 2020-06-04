package com.zgf.itc.controller;


import com.zgf.itc.entity.Rights;
import com.zgf.itc.service.RightsService;
import com.zgf.itc.utils.ResponseResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ZGF
 */
@RestController
@RequestMapping("/rights")
public class RightsController {

    @Resource
    private RightsService service;
    /**
     * 获取权限列表
     * @param type list:tree
     * @return
     */
    @GetMapping("{type}")
    public ResponseResult getByType(@PathVariable("type") String type
            ,@RequestParam(name = "roleId",required = false) Integer roleId) {
        try {
            List list = null;
            if (roleId == null || roleId == 0) {
                if ("list".equals(type)) {
                    //返回列表
                    list = this.service.list();
                } else if ("tree".equals(type)) {
                    //返回树形
                    list = this.service.getTree();
                }
            }else {
                list  = this.service.getMenuByRoleId(roleId);
            }

            return ResponseResult.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @PutMapping
    public ResponseResult update(@RequestBody Rights entity) {
        try {
            if (this.service.updateById(entity)) {
                return ResponseResult.ok();
            } else {
                return ResponseResult.error();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @GetMapping("article/{roleId}")
    public ResponseResult canOperateArticle(@PathVariable("roleId") Integer roleId) {
        if (this.service.canOperateArticle(roleId)) {
            return ResponseResult.ok();
        } else {
            return ResponseResult.error();
        }
    }
}

