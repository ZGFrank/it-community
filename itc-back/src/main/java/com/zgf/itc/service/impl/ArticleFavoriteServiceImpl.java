package com.zgf.itc.service.impl;

import com.zgf.itc.entity.*;
import com.zgf.itc.mapper.ArticleFavoriteMapper;
import com.zgf.itc.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgf.itc.vo.FavoriteArticleItem;
import com.zgf.itc.vo.MyFavorite;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ZGF
 */
@Service
public class ArticleFavoriteServiceImpl extends ServiceImpl<ArticleFavoriteMapper, ArticleFavorite> implements ArticleFavoriteService {

    @Resource
    private FavoriteFolderService ffs;
    @Resource
    private ArticleService as;
    @Resource
    private UserService us;
    @Resource
    private MessageService ms;
    @Override
    public boolean saveFavorite(ArticleFavorite favorite) {
        favorite.setCreateTime(LocalDateTime.now());
        boolean save = this.save(favorite);
        //给当前文章作者发送消息
        Article one = this.as.query().select("u_id","title").eq("art_id", favorite.getArtId()).one();
        User user = this.us.query().select("nickname").eq("u_id", one.getuId()).one();
        Message message = new Message();
        message.setCreateTime(LocalDateTime.now());
        message.setSendId(favorite.getuId());
        message.setRecvId(one.getuId());
        message.setContent("<a href='/#/userPage/"+favorite.getuId()+"'><cite>"+user.getNickname()
                +"</cite></a><span class='remind'>收藏</span>了您的文章<a href='/#/articleDetail/"
                +favorite.getArtId()+"'><cite>《"+one.getTitle()+"》</cite></a>");
        this.ms.save(message);
        //获取当前文章的总收藏数
        Integer count = this.baseMapper.getAllCollectCount(favorite.getArtId());
        if (count >= 40) {
            //如果收藏数大于40，文章设置为精华
            //发送消息
            message.setContent("您的文章<a href='/#/articleDetail/"+favorite.getArtId()
                    +"'>《"+one.getTitle()+"》</a>被设置为<span class='remind'>精华</span>");
            message.setSendId(null);
            message.setMsgId(null);
            this.ms.save(message);
            return this.as.update().set("status",1).eq("art_id",favorite.getArtId()).update();
        }
        return save;
    }

    @Override
    public Integer getFidByUserAndArtId(Integer artId, Integer uId) {
        ArticleFavorite favorite = this.query()
                .eq("art_id", artId)
                .eq("u_id", uId)
                .select("f_id")
                .one();
        if (favorite == null) {
            return null;
        }
        return favorite.getfId();
    }

    @Override
    public List<MyFavorite> getFavoritesByUid(Integer uId) {
        List<MyFavorite> myFavorites = new ArrayList<>(5);
        //通过uId获取文件夹列表
        List<FavoriteFolder> folders = this.ffs.query().eq("u_id", uId).list();
        if (folders != null && folders.size() > 0) {
            //将数据进行封装
            for (FavoriteFolder folder : folders) {
                MyFavorite build = MyFavorite.builder()
                        .dId(folder.getdId())
                        .dirname(folder.getDirname())
                        .state(folder.getState())
                        .createTime(folder.getCreateTime()).build();
                //获取文件夹下的文章收藏
                List<FavoriteArticleItem> list = this.as.getArticlesByDirId(folder.getdId());
                //将文章收藏放入文件夹中
                build.setList(list);
                //将文件夹放入文件夹列表中
                myFavorites.add(build);
            }
            return myFavorites;
        }

        return null;
    }

}
