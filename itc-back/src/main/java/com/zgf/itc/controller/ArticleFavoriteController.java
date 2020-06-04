package com.zgf.itc.controller;


import com.zgf.itc.entity.ArticleFavorite;
import com.zgf.itc.service.ArticleFavoriteService;
import com.zgf.itc.utils.ResponseResult;
import com.zgf.itc.vo.MyFavorite;
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
@RequestMapping("/articleFavorite")
public class ArticleFavoriteController {

    @Resource
    private ArticleFavoriteService service;


    @GetMapping("{artId}/user/{uId}")
    public ResponseResult get(@PathVariable("artId") Integer artId, @PathVariable("uId") Integer uId) {
        try {
            Integer fId = this.service.getFidByUserAndArtId(artId, uId);
            return ResponseResult.ok(fId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }
    /**
     * 保存收藏
     * @param favorite
     * @return
     */
    @PostMapping
    public ResponseResult save(@RequestBody ArticleFavorite favorite) {
        if (this.service.saveFavorite(favorite)) {
            return ResponseResult.ok(favorite.getfId());
        } else {
            return ResponseResult.e500();
        }
    }

    /**
     * 删除收藏
     * @param fId
     * @return
     */
    @DeleteMapping("{fId}")
    public ResponseResult delete(@PathVariable("fId") Integer fId) {
        if (this.service.removeById(fId)) {
            return ResponseResult.ok();
        } else {
            return ResponseResult.e500();
        }
    }

    @GetMapping("{uId}")
    public ResponseResult gets(@PathVariable("uId") Integer uId) {
        try {
            List<MyFavorite> list =  this.service.getFavoritesByUid(uId);
            return ResponseResult.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

}

