package com.zgf.itc.utils;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author ZGF
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class QueryCondition extends Pagination {

    //前台case
    private Integer category;
    private Integer type;
    private Integer condition;
    private Integer yearSpace;
    private Boolean orderByTime;
    private Integer uId;

    //后台文章
    private String title;
    private Integer isOver;
    private Integer sort;

    //后台case
    private Integer state;

    private String name;
    private String nickname;
    private String classGrade;
    private Integer gender;
}
