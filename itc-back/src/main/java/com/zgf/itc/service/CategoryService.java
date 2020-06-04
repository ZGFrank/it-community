package com.zgf.itc.service;

import com.zgf.itc.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ZGF
 */
public interface CategoryService extends IService<Category> {

    List<Category> loads(int state, String available);

    boolean updateCate(Category entity);

    boolean saveCate(Category entity);

    List<Category> loadForFront();
}
