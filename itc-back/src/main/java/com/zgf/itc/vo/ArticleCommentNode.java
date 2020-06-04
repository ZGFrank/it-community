package com.zgf.itc.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZGF
 */
@Data
@AllArgsConstructor
@ToString
public class ArticleCommentNode {

    private Integer uId;
    private String nickname;
    private String avatar;
    private Integer vip;
    private Integer artCId;
    private Integer pId;
    private String content;
    private LocalDateTime createTime;
    private Integer isTake;
    private Integer hitsZan;
}
