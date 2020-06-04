package com.zgf.itc.service;

import com.zgf.itc.entity.Rights;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zgf.itc.vo.RightsNode;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ZGF
 */
public interface RightsService extends IService<Rights> {

    List<RightsNode> getTree();

    List<Rights> getRightsByRoleId(Integer id);

    List<RightsNode> listToTree(List<Rights> rights);

    List<RightsNode> getMenuByRoleId(Integer roleId);

    boolean canOperateArticle(Integer roleId);
}
