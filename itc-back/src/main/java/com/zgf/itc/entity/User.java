package com.zgf.itc.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zgf.itc.utils.ExcelFields;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ZGF
 */
@TableName("ic_user")
public class User implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "u_id", type = IdType.AUTO)
    private Integer uId;

    /**
     * 姓名
     */
    @ExcelFields("姓名")
    private String name;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 学号或工号
     */
    @ExcelFields("学号")
    private String account;

    /**
     * 密码
     */
    @JsonIgnore
    private String password;

    @ExcelFields("电话")
    private String phone;

    @ExcelFields("邮箱")
    private String email;

    /**
     * 班级
     */
    @ExcelFields("班级")
    private String classGrade;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别(1：男，0：女)
     */
    @ExcelFields("性别")
    private Integer gender;

    /**
     * 签名
     */
    private String signature;

    private Integer learnCoin;

    private String city;

    private LocalDateTime createTime;

    /**
     * 邮箱是否激活
     */
    private Integer state;

    private Integer vip;

    private Integer roleId;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getVip() {
        return vip;
    }

    public void setVip(Integer vip) {
        this.vip = vip;
    }

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
        if (uId == null && password == null) {
            password = DigestUtils.md5DigestAsHex(account.getBytes());
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClassGrade() {
        return classGrade;
    }

    public void setClassGrade(String classGrade) {
        this.classGrade = classGrade;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Integer getLearnCoin() {
        return learnCoin;
    }

    public void setLearnCoin(Integer learnCoin) {
        this.learnCoin = learnCoin;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "User{" +
        "uId=" + uId +
        ", name=" + name +
        ", nickname=" + nickname +
        ", account=" + account +
        ", password=" + password +
        ", phone=" + phone +
        ", email=" + email +
        ", classGrade=" + classGrade +
        ", avatar=" + avatar +
        ", gender=" + gender +
        ", signature=" + signature +
        ", learnCoin=" + learnCoin +
        ", city=" + city +
        ", createTime=" + createTime +
        ", state=" + state +
        "}";
    }
}
