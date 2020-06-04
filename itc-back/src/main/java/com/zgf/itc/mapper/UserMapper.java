package com.zgf.itc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgf.itc.entity.User;
import com.zgf.itc.vo.Admin;
import com.zgf.itc.vo.TopCommentUser;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ZGF
 */
public interface UserMapper extends BaseMapper<User> {

    List<TopCommentUser> getWeekTop();

    List<Admin> getAdmins();

    IPage<User> getAll(Page page, String name, String nickname, String classGrade, Integer gender);

    List<Integer> getUserIdsByRightsId(int rightsId);
}
