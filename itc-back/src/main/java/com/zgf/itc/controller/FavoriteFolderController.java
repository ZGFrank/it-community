package com.zgf.itc.controller;


import com.zgf.itc.entity.FavoriteFolder;
import com.zgf.itc.service.FavoriteFolderService;
import com.zgf.itc.utils.ResponseResult;
import com.zgf.itc.vo.FavoriteFolderVo;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * 用户收藏文件夹
 * </p>
 *
 * @author ZGF
 */
@RestController
@RequestMapping("/favoriteFolder")
public class FavoriteFolderController {

    @Resource
    private FavoriteFolderService service;

    @GetMapping("{uId}")
    public ResponseResult load(@PathVariable("uId") Integer uId) {

        try {
            List<FavoriteFolderVo> folders = this.service.getFavoriteFolders(uId);
            return ResponseResult.ok(folders);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @PostMapping
    public ResponseResult save(@RequestBody FavoriteFolder folder) {
        if (this.service.saveFolder(folder)) {
            return ResponseResult.ok();
        } else {
            return ResponseResult.e500();
        }
    }


    @PutMapping
    public ResponseResult update(@RequestBody FavoriteFolder folder) {
        if (this.service.updateFolder(folder)) {
            return ResponseResult.ok();
        } else {
            return ResponseResult.e500();
        }
    }

    @DeleteMapping("{dId}")
    public ResponseResult delete(@PathVariable("dId")Integer dId) {
        if (this.service.removeById(dId)) {
            return ResponseResult.ok();
        } else {
            return ResponseResult.e500();
        }
    }
}

