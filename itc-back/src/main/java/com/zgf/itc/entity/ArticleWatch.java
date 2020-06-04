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
 * @author ZGF
 * @since 2020-03-09
 */
@TableName("ic_article_watch")
public class ArticleWatch implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "art_w_id", type = IdType.AUTO)
    private Integer artWId;

    private Integer artId;

    private Integer uId;

    private LocalDateTime createTime;


    public Integer getArtWId() {
        return artWId;
    }

    public void setArtWId(Integer artWId) {
        this.artWId = artWId;
    }

    public Integer getArtId() {
        return artId;
    }

    public void setArtId(Integer artId) {
        this.artId = artId;
    }

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ArticleWatch{" +
        "artWId=" + artWId +
        ", artId=" + artId +
        ", uId=" + uId +
        ", createTime=" + createTime +
        "}";
    }
}
