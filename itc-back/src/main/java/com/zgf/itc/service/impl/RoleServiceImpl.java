package com.zgf.itc.service.impl;

import com.zgf.itc.entity.Rights;
import com.zgf.itc.entity.Role;
import com.zgf.itc.mapper.RoleMapper;
import com.zgf.itc.service.RightsService;
import com.zgf.itc.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgf.itc.vo.RightsNode;
import com.zgf.itc.vo.RoleNode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ZGF
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private RightsService rs;
    @Override
    public List<RoleNode> getAll() {
        //List<>this.baseMapper
        List<Role> list = this.query().list();
        if (list == null || list.size() == 0) {
            return null;
        }
        List<RoleNode> nodes = new ArrayList<>();
        for (Role role : list) {
            RoleNode roleNode = this.roleToRoleNode(role);
            nodes.add(roleNode);
        }
        return nodes;
    }

    @Override
    public boolean saveRole(Role entity) {
        entity.setCreateTime(LocalDateTime.now());
        return this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void allotRights(Integer roleId, List<Integer> rightSids) {
        //先删除该角色下的所有权限
        this.baseMapper.deleteRightsByRoleId(roleId);
        //再分配角色
        for (Integer rid : rightSids) {
            this.baseMapper.insertRoleRights(roleId, rid);
        }
    }

    @Override
    public List<RightsNode> deleteRightById(Integer roleId, Integer rightId) {
        this.baseMapper.deleteRightById(roleId,rightId);
        List<Rights> rights = this.rs.getRightsByRoleId(roleId);
        return this.rs.listToTree(rights);
    }

    private RoleNode roleToRoleNode(Role role){
        List<Rights> rights = this.rs.getRightsByRoleId(role.getId());
        RoleNode roleNode = new RoleNode();
        roleNode.setId(role.getId());
        roleNode.setRoleName(role.getRoleName());
        roleNode.setRoleDesc(role.getRoleDesc());
        roleNode.setCreateTime(role.getCreateTime());
        roleNode.setAvailable(role.getAvailable());
        roleNode.setChildren(this.rs.listToTree(rights));
        return roleNode;
    }
    @Override
    public boolean deleteRole(Integer roleId) {
        //先删除该角色下的所有权限
        this.baseMapper.deleteRightsByRoleId(roleId);
        //在删除该角色
        return this.removeById(roleId);
    }

}
