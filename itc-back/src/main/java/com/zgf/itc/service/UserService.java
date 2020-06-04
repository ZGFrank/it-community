package com.zgf.itc.service;

import com.zgf.itc.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zgf.itc.utils.QueryCondition;
import com.zgf.itc.vo.Admin;
import com.zgf.itc.vo.LoginInfo;
import com.zgf.itc.vo.ShowData;
import com.zgf.itc.vo.TopCommentUser;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ZGF
 */
public interface UserService extends IService<User> {

    User login(LoginInfo info);

    boolean updatePass(Integer uId, String newPass);

    User getBaseInfo(Integer uId);

    List<TopCommentUser> getWeekTop();

    List<Admin> getAdmins();

    User getUserByAccount(Integer account);

    boolean updateUserRole(Integer uId, Integer roleId);

    ShowData getAll(QueryCondition qc);

    void resetPassword(Integer uId);

    void insert(User entity);

    List<Integer> getUserIdsByRightsId(int rightsId);
}
