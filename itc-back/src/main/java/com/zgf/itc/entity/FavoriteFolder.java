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
 */
@TableName("ic_favorite_folder")
public class FavoriteFolder implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "d_id", type = IdType.AUTO)
    private Integer dId;

    /**
     * 所属用户ID
     */
    private Integer uId;

    /**
     * 文件夹名称
     */
    private String dirname;

    private LocalDateTime createTime;

    private Integer state;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getdId() {
        return dId;
    }

    public void setdId(Integer dId) {
        this.dId = dId;
    }
    public void setdid(Integer dId) {
        this.dId = dId;
    }

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public String getDirname() {
        return dirname;
    }

    public void setDirname(String dirname) {
        this.dirname = dirname;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "FavoriteFolder{" +
        "dId=" + dId +
        ", uId=" + uId +
        ", dirname=" + dirname +
        ", createTime=" + createTime +
        "}";
    }
}
