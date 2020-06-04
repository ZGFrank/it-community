package com.zgf.itc.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author ZGF
 */
@Data
public class RecentComment {

    private Integer artCId;
    private Integer pId;
    private Integer artId;
    private Integer uId;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;
    private String  tonickname;
    private String title;

}
