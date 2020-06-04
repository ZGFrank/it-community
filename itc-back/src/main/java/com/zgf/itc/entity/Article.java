package com.zgf.itc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.beans.Transient;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 *
 * </p>
 *
 * @author ZGF
 */
@Document(indexName = "article", type = "docs", shards = 1, replicas = 0)
@TableName("ic_article")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "art_id", type = IdType.AUTO)
    @Id
    @Field(type = FieldType.Integer, store = true)
    private Integer artId;

    /**
     * 用户表外键
     */
    private Integer uId;

    @Field(type = FieldType.Text, searchAnalyzer = "ik_max_word", analyzer = "ik_max_word", store = true)
    private String title;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;

    /**
     * 文章具体类型
     */
    private Integer cId;

    /**
     * 文章标签
     */
    @Field(type = FieldType.Text, searchAnalyzer = "ik_max_word", analyzer = "ik_max_word", store = true)
    private String tag;

    /**
     * 文章附件ID
     */
    private Integer attId;

    /**
     * 悬赏学币
     */
    private Integer learnCoin;

    /**
     * 是否完结
     */
    private Integer isOver;

    /**
     * 状态 1精华
     */
    private Integer status;

    /**
     * 置顶
     */
    private Integer top;

    /**
     * 置顶失效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime tExpiryTime;

    /**
     * 点赞
     */
    private Integer hitsZan;

    /**
     * 评论
     */
    private Integer hitsComment;

    /**
     * 查看
     */
    private Integer watch;

    public Article() {

    }

    public Article(Integer artId, String title, String tag) {
        this.artId = artId;
        this.title = title;
        this.tag = tag;
    }

    public LocalDateTime gettExpiryTime() {
        return tExpiryTime;
    }

    public void settExpiryTime(LocalDateTime tExpiryTime) {
        this.tExpiryTime = tExpiryTime;
    }

    public void setTopTime(String topTime) {
        this.tExpiryTime = LocalDateTime.parse(topTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Integer getcId() {
        return cId;
    }

    public void setcId(Integer cId) {
        this.cId = cId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getAttId() {
        return attId;
    }

    public void setAttId(Integer attId) {
        this.attId = attId;
    }

    public Integer getLearnCoin() {
        return learnCoin;
    }

    public void setLearnCoin(Integer learnCoin) {
        this.learnCoin = learnCoin;
    }

    public Integer getIsOver() {
        return isOver;
    }

    public void setIsOver(Integer isOver) {
        this.isOver = isOver;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public Integer getHitsZan() {
        return hitsZan;
    }

    public void setHitsZan(Integer hitsZan) {
        this.hitsZan = hitsZan;
    }

    public Integer getHitsComment() {
        return hitsComment;
    }

    public void setHitsComment(Integer hitsComment) {
        this.hitsComment = hitsComment;
    }

    public Integer getWatch() {
        return watch;
    }

    public void setWatch(Integer watch) {
        this.watch = watch;
    }

    @Override
    public String toString() {
        return "Article{" +
                "artId=" + artId +
                ", uId=" + uId +
                ", title=" + title +
                ", content=" + content +
                ", createTime=" + createTime +
                ", cId=" + cId +
                ", tag=" + tag +
                ", attId=" + attId +
                ", learnCoin=" + learnCoin +
                ", isOver=" + isOver +
                ", status=" + status +
                ", top=" + top +
                ", hitsZan=" + hitsZan +
                ", hitsComment=" + hitsComment +
                ", watch=" + watch +
                "}";
    }
}
