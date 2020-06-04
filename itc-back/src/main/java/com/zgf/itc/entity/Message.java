package com.zgf.itc.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ZGF
 * @since 2020-04-12
 */
@TableName("ic_message")
public class Message implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "msg_id", type = IdType.AUTO)
    private Integer msgId;

    private Integer sendId;

    /**
     * 为0，则是管理员发送
     */
    private Integer recvId;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;


    public Integer getMsgId() {
        return msgId;
    }

    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }

    public Integer getSendId() {
        return sendId;
    }

    public void setSendId(Integer sendId) {
        this.sendId = sendId;
    }

    public Integer getRecvId() {
        return recvId;
    }

    public void setRecvId(Integer recvId) {
        this.recvId = recvId;
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

    @Override
    public String toString() {
        return "Message{" +
        "msgId=" + msgId +
        ", sendId=" + sendId +
        ", recvId=" + recvId +
        ", content=" + content +
        ", createTime=" + createTime +
        "}";
    }
}
