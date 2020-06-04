package com.zgf.itc.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgf.itc.entity.Article;
import com.zgf.itc.entity.User;
import com.zgf.itc.mapper.UserMapper;
import com.zgf.itc.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgf.itc.utils.QueryCondition;
import com.zgf.itc.vo.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ZGF
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Value("${upload.default-avatar}")
    private String avatar;

    @Override
    public User login(LoginInfo info) {
        return this.query()
                .eq("account", info.getAccount())
                .eq("password", DigestUtils.md5DigestAsHex(info.getPassword().getBytes()))
                .one();
    }

    @Override
    public boolean updatePass(Integer uId, String newPass) {
        return this.update()
                .eq("u_id", uId)
                .set("password", DigestUtils.md5DigestAsHex(newPass.getBytes()))
                .update();
    }

    @Override
    public User getBaseInfo(Integer uId) {
        return this.query()
                .eq("u_id",uId)
                .select("u_id","name","nickname","class_grade","avatar","gender","signature","learn_coin","city","vip")
                .one();
    }

    @Override
    public List<TopCommentUser> getWeekTop() {
        return this.baseMapper.getWeekTop();
    }

    @Override
    public List<Admin> getAdmins() {
        return this.baseMapper.getAdmins();
    }

    @Override
    public User getUserByAccount(Integer account) {
        return this.query().eq("account",account).select("u_id","name").one();
    }

    @Override
    public boolean updateUserRole(Integer uId, Integer roleId) {
        return this.update().eq("u_id",uId).set("role_id",roleId).update();
    }

    @Override
    public ShowData getAll(QueryCondition qc) {
        IPage<User> list = this.baseMapper.getAll(new Page(qc.getCurrentPage(), qc.getPageSize()), qc.getName(),qc.getNickname(),qc.getClassGrade(),qc.getGender());
        return ShowData.builder().total(list.getTotal()).data(list.getRecords()).build();
    }

    @Override
    public void resetPassword(Integer uId) {
        //先获取用户
        User user = this.getById(uId);
        user.setPassword(DigestUtils.md5DigestAsHex(user.getAccount().getBytes()));
        this.updateById(user);
    }

    @Override
    public void insert(User entity) {
        entity.setPassword(DigestUtils.md5DigestAsHex(entity.getAccount().getBytes()));
        entity.setCreateTime(LocalDateTime.now());
        entity.setNickname(entity.getName());
        entity.setAvatar(avatar);
        this.save(entity);
    }

    @Override
    public List<Integer> getUserIdsByRightsId(int rightsId) {
        List<Integer> ids = this.baseMapper.getUserIdsByRightsId(rightsId);
        User user = this.query().eq("role_id", 0).one();
        if (ids == null) {
            ids = new ArrayList<>(1);
        }
        ids.add(user.getuId());
        return ids;
    }
}
