package com.zgf.itc.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author ZGF
 */
@TableName("ic_news")
public class News implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "news_id", type = IdType.AUTO)
    private Integer newsId;

    private Integer sysuId;

    private String title;

    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;

    /**
     * 失效日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime expiryTime;

    private Integer level;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getNewsId() {
        return newsId;
    }

    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
    }

    public Integer getSysuId() {
        return sysuId;
    }

    public void setSysuId(Integer sysuId) {
        this.sysuId = sysuId;
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

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    public void setExpiryDate(String expiryTime) throws ParseException {
        //Instant instant = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(expiryTime).toInstant();
        //ZoneId zoneId = ZoneId.systemDefault();
        //this.expiryTime = instant.atZone(zoneId).toLocalDateTime();
        this.expiryTime = LocalDateTime.parse(expiryTime,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    /*public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }*/

    @Override
    public String toString() {
        return "News{" +
        "newsId=" + newsId +
        ", sysuId=" + sysuId +
        ", title=" + title +
        ", content=" + content +
        ", createTime=" + createTime +
        ", expiryTime=" + expiryTime +
        "}";
    }
}
