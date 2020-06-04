package com.zgf.itc.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 文章评论表
 * </p>
 *
 * @author ZGF
 */
@TableName("ic_article_comment")
public class ArticleComment implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "art_c_id", type = IdType.AUTO)
    private Integer artCId;

    /**
     * 父评论ID，为空时，表示评论的是文章，为art_c_id对应的值是为评论别人的回复
     */
    private Integer pId;

    /**
     * 评论文章ID
     */
    private Integer artId;

    private String content;

    private LocalDateTime createTime;

    /**
     * 评论人ID
     */
    private Integer uId;

    /**
     * 是否采纳
     */
    private Integer isTake;

    /**
     * 点赞数
     */
    private Integer hitsZan;


    public Integer getArtCId() {
        return artCId;
    }

    public void setArtCId(Integer artCId) {
        this.artCId = artCId;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public Integer getArtId() {
        return artId;
    }

    public void setArtId(Integer artId) {
        this.artId = artId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public Integer getIsTake() {
        return isTake;
    }

    public void setIsTake(Integer isTake) {
        this.isTake = isTake;
    }

    public Integer getHitsZan() {
        return hitsZan;
    }

    public void setHitsZan(Integer hitsZan) {
        this.hitsZan = hitsZan;
    }

    @Override
    public String toString() {
        return "ArticleComment{" +
        "artCId=" + artCId +
        ", pId=" + pId +
        ", artId=" + artId +
        ", content=" + content +
        ", createTime=" + createTime +
        ", uId=" + uId +
        ", isTake=" + isTake +
        ", hitsZan=" + hitsZan +
        "}";
    }
}
