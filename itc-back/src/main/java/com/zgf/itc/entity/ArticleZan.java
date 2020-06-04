package com.zgf.itc.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ZGF
 */
@TableName("ic_article_zan")
public class ArticleZan implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "art_hz_id", type = IdType.AUTO)
    private Integer artHzId;

    private Integer artId;

    private Integer uId;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createTime;


    public Integer getArtHzId() {
        return artHzId;
    }

    public void setArtHzId(Integer artHzId) {
        this.artHzId = artHzId;
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
        return "ArticleZan{" +
        "artHzId=" + artHzId +
        ", artId=" + artId +
        ", uId=" + uId +
        ", createTime=" + createTime +
        "}";
    }
}
