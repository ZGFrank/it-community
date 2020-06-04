package com.zgf.itc.service.impl;

import com.zgf.itc.entity.Rights;
import com.zgf.itc.mapper.RightsMapper;
import com.zgf.itc.service.RightsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgf.itc.vo.RightsNode;
import org.springframework.stereotype.Service;

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
public class RightsServiceImpl extends ServiceImpl<RightsMapper, Rights> implements RightsService {

    @Override
    public List<RightsNode> getTree() {
        return leafGetTree(0);
    }

    @Override
    public List<Rights> getRightsByRoleId(Integer id) {
        return this.baseMapper.getRightsByRoleId(id);
    }

    private List<RightsNode> leafGetTree(Integer pid) {
        List<Rights> ls = this.query().eq("pid",pid).list();
        List<RightsNode> nodes = null;
        if (ls == null || ls.size() == 0) {
            return null;
        }
        nodes = new ArrayList<>();
        for (Rights l : ls) {
            nodes.add(RightsNode.builder()
                    .id(l.getId()).pid(l.getPid()).authName(l.getAuthName())
                    .path(l.getPath()).icon(l.getIcon()).available(l.getAvailable())
                    .children(this.leafGetTree(l.getId())).build());
        }
        return nodes;
    }

    @Override
    public List<RightsNode> listToTree(List<Rights> rights) {
        if (rights == null || rights.size() == 0) {
            return null;
        }
        List<RightsNode> nodes = new ArrayList<>(10);
        for (Rights right : rights) {
            if (right.getPid() == 0) {
                RightsNode pNode = RightsNode.builder()
                        .id(right.getId()).authName(right.getAuthName())
                        .path(right.getPath()).icon(right.getIcon()).children(new ArrayList<>()).build();
                for (Rights right1 : rights) {
                    if (right1.getPid().equals(right.getId())) {
                        pNode.getChildren().add(RightsNode.builder()
                                .id(right1.getId()).authName(right1.getAuthName())
                                .available(right1.getAvailable())
                                .path(right1.getPath()).icon(right1.getIcon()).build());
                    }
                }
                nodes.add(pNode);
            }
        }
        return nodes;
    }

    @Override
    public List<RightsNode> getMenuByRoleId(Integer roleId) {
        return this.listToTree(this.getRightsByRoleId(roleId));
    }

    @Override
    public boolean canOperateArticle(Integer roleId) {
        return this.baseMapper.getCount(roleId, "articleManager") > 0;
    }
}
