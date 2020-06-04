package com.zgf.itc.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ZGF
 * @since 2020-03-13
 */
@TableName("ic_signIn")
public class SignIn implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "s_id", type = IdType.AUTO)
    private Integer sId;

    private Integer uId;

    private LocalDateTime createTime;

    private Integer days;


    public Integer getsId() {
        return sId;
    }

    public void setsId(Integer sId) {
        this.sId = sId;
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

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    @Override
    public String toString() {
        return "SignIn{" +
        "sId=" + sId +
        ", uId=" + uId +
        ", createTime=" + createTime +
        ", days=" + days +
        "}";
    }
}
