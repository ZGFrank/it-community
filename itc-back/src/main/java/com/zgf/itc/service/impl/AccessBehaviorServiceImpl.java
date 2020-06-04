package com.zgf.itc.service.impl;

import com.zgf.itc.entity.AccessBehavior;
import com.zgf.itc.mapper.AccessBehaviorMapper;
import com.zgf.itc.service.AccessBehaviorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ZGF
 */
@Service
public class AccessBehaviorServiceImpl extends ServiceImpl<AccessBehaviorMapper, AccessBehavior> implements AccessBehaviorService {

    @Override
    public void insert(AccessBehavior entity) {
        //判断当前文章用户是否已经有访问行为
        AccessBehavior old = this.query()
                .eq("u_id", entity.getuId())
                .eq("art_id", entity.getArtId())
                .one();
        entity.setCreateTime(LocalDateTime.now());
        if (old == null) {
            this.save(entity);
        }else {
            if (old.getRating() < entity.getRating()) {
                entity.setId(old.getId());
                this.updateById(entity);
            }
        }
    }
}
