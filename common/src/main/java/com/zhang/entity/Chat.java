package com.zhang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhang
 * @date 2024/3/24
 * @Description
 */
@TableName("chat")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Chat implements Serializable {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField("from_user_id")
    private Long fromUserId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField("to_user_id")
    private Long toUserId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField("group_id")
    private int  groupId;
    private String content;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField( "created_at")
    private Date createdAt;
    private boolean status;//消息接收状态

    public Chat(Long id, Long fromUserId, Long toUserId, String content, Date createdAt, boolean status) {
        this.id = id;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.content = content;
        this.createdAt = createdAt;
        this.status = status;
    }

    public Chat(Long id, Long fromUserId, int groupId, String content, Date createdAt) {
        this.id = id;
        this.fromUserId = fromUserId;
        this.groupId = groupId;
        this.content = content;
        this.createdAt = createdAt;
    }
}
