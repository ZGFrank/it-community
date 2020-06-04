package com.zgf.itc.controller;


import com.zgf.itc.entity.AccessBehavior;
import com.zgf.itc.entity.User;
import com.zgf.itc.service.AccessBehaviorService;
import com.zgf.itc.service.UserService;
import com.zgf.itc.utils.ExcelUtil;
import com.zgf.itc.utils.QueryCondition;
import com.zgf.itc.utils.ResponseResult;
import com.zgf.itc.utils.TokenUtil;
import com.zgf.itc.vo.Admin;
import com.zgf.itc.vo.PassVo;
import com.zgf.itc.vo.ShowData;
import com.zgf.itc.vo.TopCommentUser;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService service;
    @Resource
    private AccessBehaviorService abs;
    @Resource
    private TokenUtil tokenUtil;
    @Resource
    private HttpServletRequest request;
    @Resource
    private ExcelUtil excelUtil;

    @PostMapping
    public ResponseResult save(@RequestBody User entity) {
        try {
            this.service.insert(entity);
            return ResponseResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @PostMapping("import")
    public ResponseResult importUser(@RequestParam("file") MultipartFile file) throws IOException {
        List<User> users = excelUtil.readExcelFileToDTO(file, User.class);
        if (users != null && users.size() > 0) {
            if (service.saveBatch(users)) {
                return ResponseResult.ok();
            }
            return ResponseResult.error();
        }
        return ResponseResult.e500();
    }

    @PutMapping("update")
    public ResponseResult update(@RequestBody User user) {
        if (!tokenUtil.isExist(request)) {
            return ResponseResult.error("验证失败");
        }
        boolean update = this.service.updateById(user);
        if (update) {
            return ResponseResult.ok("设置成功");
        }
        return ResponseResult.error("设置失败");
    }

    @DeleteMapping("{uId}")
    public ResponseResult deleteById(@PathVariable("uId") Integer uId) {
        if (!tokenUtil.isExist(request)) {
            return ResponseResult.error("验证失败");
        }
        if (this.service.removeById(uId)) {
            return ResponseResult.ok("设置成功");
        }
        return ResponseResult.error("设置失败");
    }

    @DeleteMapping
    public ResponseResult deleteBatch(@RequestParam("ids") String idsStr) {
        if (!tokenUtil.isExist(request)) {
            return ResponseResult.error("验证失败");
        }
        List<Integer> ids = Arrays.stream(idsStr.split(",")).map(Integer::valueOf).collect(Collectors.toList());
        if (this.service.removeByIds(ids)) {
            return ResponseResult.ok("设置成功");
        }
        return ResponseResult.error("设置失败");
    }

    @PutMapping("pass/{uId}")
    public ResponseResult updatePass(@PathVariable("uId") Integer uId
            , @RequestBody PassVo passVo) {
        //验证用户
        if (!tokenUtil.isExist(request)) {
            return ResponseResult.error("验证失败");
        }
        User user = this.service.getById(uId);
        if (!user.getPassword().equals(DigestUtils.md5DigestAsHex(passVo.getOriginPass().getBytes()))) {
            return ResponseResult.error("密码错误");
        }
        if (this.service.updatePass(uId, passVo.getNewPass())) {
            return ResponseResult.ok("修改成功");
        } else {
            return ResponseResult.error("修改失败");
        }
    }

    @PutMapping("{uId}/role/{roleId}")
    public ResponseResult updateUserRole(@PathVariable("uId") Integer uId, @PathVariable("roleId") Integer roleId) {
        try {
            if (this.service.updateUserRole(uId, roleId)) {
                return ResponseResult.ok();

            } else {
                return ResponseResult.e404();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @GetMapping
    public ResponseResult getAll(QueryCondition qc) {
        try {
            ShowData data = this.service.getAll(qc);
            return ResponseResult.ok(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @GetMapping("base/{uId}")
    public ResponseResult getBaseInfo(@PathVariable("uId") Integer uId) {
        try {
            User user = this.service.getBaseInfo(uId);
            if (user == null) {
                return ResponseResult.e404();
            } else {
                return ResponseResult.ok(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @GetMapping("weekTop")
    public ResponseResult getWeekTop() {
        try {
            List<TopCommentUser> list = this.service.getWeekTop();
            return ResponseResult.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @GetMapping("account/{account}")
    public ResponseResult getUserByAccount(@PathVariable("account") Integer account) {
        try {
            User user = this.service.getUserByAccount(account);
            return ResponseResult.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @GetMapping("admin")
    public ResponseResult getAdmins() {
        try {
            List<Admin> list = this.service.getAdmins();
            return ResponseResult.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }

    @PostMapping("behavior")
    public ResponseResult saveUserAccessRating(@RequestBody AccessBehavior entity) {
        this.abs.insert(entity);
        return ResponseResult.ok();
    }

    @PutMapping("reset/{uId}")
    public ResponseResult resetPassword(@PathVariable("uId") Integer uId) {
        try {
            this.service.resetPassword(uId);
            return ResponseResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.e500();
        }
    }
}

