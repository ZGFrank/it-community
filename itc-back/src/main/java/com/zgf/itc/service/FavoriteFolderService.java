package com.zgf.itc.service;

import com.zgf.itc.entity.FavoriteFolder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zgf.itc.vo.FavoriteFolderVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ZGF
 */
public interface FavoriteFolderService extends IService<FavoriteFolder> {

    List<FavoriteFolderVo> getFavoriteFolders(Integer uId);

    boolean saveFolder(FavoriteFolder folder);

    boolean updateFolder(FavoriteFolder folder);

    boolean deleteFolder(Integer dId);
}
