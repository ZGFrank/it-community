package com.zgf.itc.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ZGF
 */
@TableName("ic_category")
public class Category implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "c_id", type = IdType.AUTO)
    private Integer cId;

    private String name;

    /**
     * 是否可用 
     */
    private Integer available;

    /**
     * 普通用户是否可用
     */
    private Integer state;

    private String pinyin;


    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public Integer getcId() {
        return cId;
    }

    public void setcId(Integer cId) {
        this.cId = cId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Category{" +
        "cId=" + cId +
        ", name=" + name +
        ", available=" + available +
        ", state=" + state +
        "}";
    }
}
