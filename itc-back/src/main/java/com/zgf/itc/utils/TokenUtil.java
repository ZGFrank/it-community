package com.zgf.itc.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author ZGF
 */
@Component
public class TokenUtil {
    @Resource
    private  RedisTemplate<String,String> redisTemplate;
    /**
     * 生成token(格式为token:设备-加密的用户名-时间-六位随机数)
     *
     * @param username 用户登录名
     * @return
     */
    public static String generateToken(String username) {
        StringBuilder token = new StringBuilder();
        //加token:
        token.append("token:")
                //加加密的用户名
                .append(DigestUtils.md5DigestAsHex(username.getBytes()))
                .append("-")
                //加时间
                .append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()))
                .append("-")
                //加六位随机数111111-999999
                .append(new Random().nextInt((999999 - 111111 + 1)) + 111111);
        System.out.println("token=>" + token.toString());
        return token.toString();
    }

    public String saveToken(Integer uid,String account){
        String token = generateToken(account);
        redisTemplate.opsForValue().set(token, uid.toString(), 2, TimeUnit.HOURS);
        return token;
    }

    public void save(String key, String value,long l,TimeUnit t){
        redisTemplate.opsForValue().set(key, value, l, t);
    }
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }



    public boolean isExist(HttpServletRequest request){
        //从请求头中获取token
        String token = request.getHeader("Authorization");
        if (token == null) {
            return false;
        }
        return redisTemplate.opsForValue().get(token) != null;
    }

    public Integer getUidByToken(HttpServletRequest request) {
        try {
            return Integer.valueOf(Objects.requireNonNull(redisTemplate.opsForValue().get(request.getHeader("Authorization"))));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
