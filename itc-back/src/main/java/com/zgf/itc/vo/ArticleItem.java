package com.zgf.itc.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author ZGF
 */
@Data
public class ArticleItem {

    private Long uId;
    private Long artId;
    private String avatar;
    private String nickname;
    private String name;
    private String title;
    private Integer vip;
    private Integer isOver;
    private Integer hitsComment;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;
    private Integer learnCoin;
}
