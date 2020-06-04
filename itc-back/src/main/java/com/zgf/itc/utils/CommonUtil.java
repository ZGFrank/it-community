package com.zgf.itc.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

/**
 * @author ZGF
 */
public class CommonUtil {

    /**
     * 获取访问用户IP
     * @param request
     * @return
     */
    public static String getVisitIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 根据天数获取今日签到赢得学币数
     *
     * @param days
     * @return
     */
    public static Integer getLearnCoin(Integer days) {
        if (days < 5) {
            return 5;
        }
        if (days < 15) {
            return 10;
        }
        if (days < 30) {
            return 15;
        }
        if (days < 100) {
            return 20;
        }
        if (days < 365) {
            return 30;
        }
        return 50;
    }

    /**
     * 生成随机用户名，数字和字母组成
     * @param length
     * @return
     */
    public static String getStringRandom(int length) {
        StringBuilder val = new StringBuilder();
        Random random = new Random();
        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val.append((char) (random.nextInt(26) + temp));
            } else {
                val.append(random.nextInt(10));
            }
        }
        return val.toString();
    }

    public static Integer getVip(Integer learnCoin) {
        if (learnCoin < 100) {
            return 0;
        } else if (learnCoin < 200) {
            return 1;
        } else if (learnCoin < 500) {
            return 2;
        } else if (learnCoin < 1000) {
            return 3;
        } else if (learnCoin < 2000) {
            return 4;
        } else if (learnCoin < 5000) {
            return 5;
        } else if (learnCoin < 10000) {
            return 6;
        } else if (learnCoin < 20000) {
            return 7;
        } else {
            return 8;
        }
    }
}
