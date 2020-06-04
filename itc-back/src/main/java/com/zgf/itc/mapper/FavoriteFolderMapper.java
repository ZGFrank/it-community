package com.zgf.itc.mapper;

import com.zgf.itc.entity.FavoriteFolder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zgf.itc.vo.FavoriteFolderVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ZGF
 */
public interface FavoriteFolderMapper extends BaseMapper<FavoriteFolder> {

    List<FavoriteFolderVo> getFavoriteFolders(Integer uId);
}
