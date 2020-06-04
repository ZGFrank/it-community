package com.zgf.itc.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ZGF\
 */
@TableName("ic_article_favorite")
public class ArticleFavorite implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "f_id", type = IdType.AUTO)
    private Integer fId;

    /**
     * 用户ID
     */
    private Integer uId;

    /**
     * 收藏于对用文件夹的ID
     */
    private Integer dirId;

    /**
     * 文章ID
     */
    private Integer artId;

    private LocalDateTime createTime;


    public Integer getfId() {
        return fId;
    }

    public void setfId(Integer fId) {
        this.fId = fId;
    }

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public Integer getDirId() {
        return dirId;
    }

    public void setDirId(Integer dirId) {
        this.dirId = dirId;
    }

    public Integer getArtId() {
        return artId;
    }

    public void setArtId(Integer artId) {
        this.artId = artId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ArticleFavorite{" +
        "fId=" + fId +
        ", uId=" + uId +
        ", dirId=" + dirId +
        ", artId=" + artId +
        ", createTime=" + createTime +
        "}";
    }
}
