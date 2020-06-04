package com.zgf.itc.service;

import com.zgf.itc.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zgf.itc.vo.RightsNode;
import com.zgf.itc.vo.RoleNode;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ZGF
 */
public interface RoleService extends IService<Role> {

    List<RoleNode> getAll();

    boolean saveRole(Role entity);

    void allotRights(Integer roleId, List<Integer> rightsids);

    List<RightsNode> deleteRightById(Integer roleId, Integer rightId);

    boolean deleteRole(Integer roleId);
}
