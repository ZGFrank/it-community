package com.zgf.itc.service.impl;

import com.zgf.itc.entity.SignIn;
import com.zgf.itc.entity.User;
import com.zgf.itc.mapper.SignInMapper;
import com.zgf.itc.service.SignInService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgf.itc.service.UserService;
import com.zgf.itc.utils.CommonUtil;
import com.zgf.itc.vo.SignInBaseInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ZGF
 */
@Service
public class SignInServiceImpl extends ServiceImpl<SignInMapper, SignIn> implements SignInService {

    @Resource
    private UserService us;
    @Override
    public SignInBaseInfo getSignInfo(Integer uid) {
        SignInBaseInfo info = null;
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime yesterday = LocalDateTime.of(LocalDate.ofYearDay(now.getYear(), now.getDayOfYear() - 1), LocalTime.MIN);
        //判断今天是否签到
        SignIn data = this.baseMapper.getByDay(uid,0);
        if (data != null) {
            Integer learnCoin = CommonUtil.getLearnCoin(data.getDays());
            info = SignInBaseInfo.builder().isSign(true).days(data.getDays()).learnCoin(learnCoin).build();
            return info;
        }
        //今天没有签到，判断昨天是否签到
        SignIn yd = this.baseMapper.getByDay(uid, 1);
        if (yd != null) {
            Integer learnCoin = CommonUtil.getLearnCoin(yd.getDays()+1);
            info = SignInBaseInfo.builder().isSign(false).days(yd.getDays()).learnCoin(learnCoin).build();
            return info;
        }
        return SignInBaseInfo.builder().isSign(false).days(0).learnCoin(CommonUtil.getLearnCoin(1)).build();
    }

    @Override
    public boolean saveRecord(Integer uid) {
        //获取昨日签到记录
        SignIn yd = this.baseMapper.getByDay(uid, 1);
        Integer learnCoin = 5;
        SignIn signIn = new SignIn();
        signIn.setuId(uid);
        signIn.setCreateTime(LocalDateTime.now());
        if (yd == null) {
            signIn.setDays(1);
        }else {
            signIn.setDays(yd.getDays()+1);
            learnCoin = CommonUtil.getLearnCoin(yd.getDays() + 1);
        }
        //保存签到信息
        if (this.save(signIn)){
            //让该用户的学币数增加对应数额
            User user = this.us.getById(uid);
            user.setLearnCoin(user.getLearnCoin()+learnCoin);
            user.setVip(CommonUtil.getVip(user.getLearnCoin()));
            return this.us.updateById(user);
        }
        return false;
    }
}
