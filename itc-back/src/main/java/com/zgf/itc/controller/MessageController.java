package com.zgf.itc.controller;


import com.zgf.itc.entity.Message;
import com.zgf.itc.service.MessageService;
import com.zgf.itc.utils.ResponseResult;
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
@RequestMapping("/message")
public class MessageController {

    @Resource
    private MessageService service;

    @GetMapping("all/{uId}")
    public ResponseResult getAll(@PathVariable("uId") Integer uId) {
        try {
            List<Message> list = this.service.getMessageByRecvId(uId);
            return ResponseResult.ok(list);
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

    @DeleteMapping("/all")
    public ResponseResult deleteBatch(@RequestParam("ids") String idsStr) {
        List<Integer> ids = Arrays.stream(idsStr.split(",")).map(Integer::valueOf).collect(Collectors.toList());
        try {
            if (this.service.removeByIds(ids)) {
                return ResponseResult.ok();
            } else {
                return ResponseResult.error();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @GetMapping("count/{uId}")
    public ResponseResult getCount(@PathVariable("uId") Integer uId) {
        try {
            Integer count = this.service.getCountByRecvId(uId);
            return ResponseResult.ok(count);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error();
        }
    }
}

