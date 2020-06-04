package com.zgf.itc.mapper;

import com.zgf.itc.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ZGF
 */
public interface RoleMapper extends BaseMapper<Role> {

    void insertRoleRights(Integer roleId, Integer rid);

    void deleteRightById(Integer roleId, Integer rightId);

    void deleteRightsByRoleId(Integer roleId);
}
