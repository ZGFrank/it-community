package com.zgf.itc.controller;


import com.zgf.itc.entity.Role;
import com.zgf.itc.service.RoleService;
import com.zgf.itc.utils.ResponseResult;
import com.zgf.itc.vo.RightsNode;
import com.zgf.itc.vo.RoleNode;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ZGF
 */
@RestController
@RequestMapping("/role")
public class RoleController {


    @Resource
    private RoleService service;

    @GetMapping
    public ResponseResult getAll() {
        try {
            List<RoleNode> list = this.service.getAll();
            return ResponseResult.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @PostMapping
    public ResponseResult save(@RequestBody Role entity) {
        try {
            if (this.service.saveRole(entity)) {
                return ResponseResult.ok();
            }else {
                return ResponseResult.error();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @PostMapping("{roleId}/rights")
    public ResponseResult allotRights(@PathVariable("roleId") Integer roleId,@RequestBody String rids) {
        try {
            String ss = rids.substring(9, rids.length()-2);
            String[] split = ss.split(",");
            List<Integer> rightsids = new ArrayList<>();
            for (String s : split) {
                rightsids.add(Integer.parseInt(s));
            }
            this.service.allotRights(roleId, rightsids);
            return ResponseResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @PutMapping
    public ResponseResult update(@RequestBody Role entity) {
        try {
            if (this.service.updateById(entity)) {
                return ResponseResult.ok();
            }else {
                return ResponseResult.error();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }


    @DeleteMapping("{roleId}/rights/{rightId}")
    public ResponseResult deleteRightById(@PathVariable("roleId") Integer roleId, @PathVariable("rightId") Integer rightId) {
        try {
            List<RightsNode> res = this.service.deleteRightById(roleId,rightId);
            return ResponseResult.ok(res);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @DeleteMapping("{roleId}")
    public ResponseResult deleteRole(@PathVariable("roleId") Integer roleId) {
        try {
            if (this.service.deleteRole(roleId)) {
                return ResponseResult.ok();
            }else {
                return ResponseResult.error();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

}

