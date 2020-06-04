package com.zgf.itc.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.zgf.itc.entity.User;
import com.zgf.itc.service.UserService;
import com.zgf.itc.utils.CommonUtil;
import com.zgf.itc.utils.ResponseResult;
import com.zgf.itc.utils.TokenUtil;
import com.zgf.itc.vo.LoginInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author ZGF
 */
@Controller
public class LoginController {

    @Resource
    private TokenUtil tokenUtil;

    @Resource
    private UserService userService;

    /**
     * 根据类型登录
     *
     * @param info 用户登录信息
     * @return
     */
    @PostMapping("login")
    @ResponseBody
    public ResponseResult login(
            @RequestBody LoginInfo info,
            HttpServletRequest request) {

        String key = CommonUtil.getVisitIP(request) + info.getCode();
        String code = this.tokenUtil.get(key);
        if (code != null) {
            this.tokenUtil.save(key, "", 3, TimeUnit.SECONDS);

            User normal = this.userService.login(info);
            if (normal == null) {
                return ResponseResult.error("用户名或密码错误");
            }
            String token = this.tokenUtil.saveToken(normal.getuId(),normal.getAccount());
            Map<String, Object> map = new HashMap<>();
            map.put("user", normal);
            map.put("token", token);
            return ResponseResult.ok("登陆成功", map);
        } else {
            return ResponseResult.error("验证码错误");
        }

    }

    /**
     * 得到登陆验证码
     *
     * @throws IOException
     */
    @GetMapping("login/code")
    public void getCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String ip = CommonUtil.getVisitIP(request);
        // 定义图形验证码的长和宽
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(116, 36, 4, 5);
        //将用户的访问主机IP+验证码组合成唯一的key值
        this.tokenUtil.save(ip + lineCaptcha.getCode(), lineCaptcha.getCode(), 2, TimeUnit.MINUTES);
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(lineCaptcha.getImage(), "JPEG", outputStream);
    }

}
