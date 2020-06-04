package com.zgf.itc.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZGF
 */
@Data
@ToString
@AllArgsConstructor
public class ArtComTreeNode {
    private Integer uId;
    private String nickname;
    private String avatar;
    private Integer vip;
    private Integer artCId;
    private Integer pId;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;
    private Integer isTake;
    private Integer hitsZan;
    private List<ArtComTreeNode> children = new ArrayList<>();
}
