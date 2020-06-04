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
 */
@TableName("ic_article_comment_zan")
public class ArticleCommentZan implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "art_c_hz_id", type = IdType.AUTO)
    private Integer artCHzId;

    /**
     * 点赞回复用户ID
     */
    private Integer uId;

    /**
     * 对应评论ID
     */
    private Integer artCId;

    private LocalDateTime createTime;


    public Integer getArtCHzId() {
        return artCHzId;
    }

    public void setArtCHzId(Integer artCHzId) {
        this.artCHzId = artCHzId;
    }

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public Integer getArtCId() {
        return artCId;
    }

    public void setArtCId(Integer artCId) {
        this.artCId = artCId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ArticleCommentZan{" +
        "artCHzId=" + artCHzId +
        ", uId=" + uId +
        ", artCId=" + artCId +
        ", createTime=" + createTime +
        "}";
    }
}
