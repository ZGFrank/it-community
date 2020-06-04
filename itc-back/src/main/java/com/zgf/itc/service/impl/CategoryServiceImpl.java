package com.zgf.itc.service.impl;

import com.zgf.itc.entity.Category;
import com.zgf.itc.mapper.CategoryMapper;
import com.zgf.itc.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgf.itc.utils.PinyinTool;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ZGF
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public List<Category> loads(int state, String available) {
        return this.query()
                .ge("state", state)
                .ge("available", available)
                .list();
    }

    @Override
    public boolean updateCate(Category entity) {
        entity.setPinyin(PinyinTool.toPinYin(entity.getName(), PinyinTool.Type.LOWERCASE));
        return this.updateById(entity);
    }

    @Override
    public boolean saveCate(Category entity) {
        entity.setPinyin(PinyinTool.toPinYin(entity.getName(), PinyinTool.Type.LOWERCASE));
        return this.save(entity);
    }

    @Override
    public List<Category> loadForFront() {
        return this.query()
                .ne("available",0)
                .orderByDesc("state")
                .list();
    }
}
