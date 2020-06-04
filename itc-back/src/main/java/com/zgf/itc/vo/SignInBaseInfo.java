package com.zgf.itc.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author ZGF
 */
@Data
@Builder
public class SignInBaseInfo {
    //连续签到天数
    private Integer days;
    //今天是否签到
    private boolean isSign;
    //今天签到可获得学币数量
    private Integer learnCoin;


}
