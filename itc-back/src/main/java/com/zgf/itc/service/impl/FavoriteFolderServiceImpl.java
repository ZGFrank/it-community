package com.zgf.itc.service.impl;

import com.zgf.itc.entity.FavoriteFolder;
import com.zgf.itc.mapper.FavoriteFolderMapper;
import com.zgf.itc.service.ArticleFavoriteService;
import com.zgf.itc.service.FavoriteFolderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgf.itc.vo.FavoriteFolderVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ZGF
 */
@Service
public class FavoriteFolderServiceImpl extends ServiceImpl<FavoriteFolderMapper, FavoriteFolder> implements FavoriteFolderService {

    @Resource
    private ArticleFavoriteService afs;
    @Override
    public List<FavoriteFolderVo> getFavoriteFolders(Integer uId) {
        List<FavoriteFolderVo> folders = new ArrayList<>(5);
        //判断该用户是否有收藏文件夹
        if (this.query().eq("u_id", uId).count() == 0) {
            //没有，则为用户创建默认文件夹
            FavoriteFolder folder = new FavoriteFolder();
            folder.setCreateTime(LocalDateTime.now());
            folder.setDirname("默认文件夹");
            folder.setuId(uId);
            folder.setState(0);
            if (this.save(folder)) {
                folders.add(FavoriteFolderVo.builder()
                        .dId(folder.getdId())
                        .dirname(folder.getDirname())
                        .createTime(folder.getCreateTime()).num(0).build());
            }
        }else {
            folders = this.baseMapper.getFavoriteFolders(uId);
        }
        return folders;
    }

    @Override
    public boolean saveFolder(FavoriteFolder folder) {
        folder.setCreateTime(LocalDateTime.now());
        return this.save(folder);
    }

    @Override
    public boolean updateFolder(FavoriteFolder folder) {
        folder.setCreateTime(LocalDateTime.now());
        return this.updateById(folder);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteFolder(Integer dId) {
        //删除文件夹中的文件
        Map<String, Object> map = new HashMap<>(1);
        map.put("dir_id", dId);
        if (this.afs.removeByMap(map)) {
            //删除文件夹
            return this.removeById(dId);
        }
        return false;
    }

}
