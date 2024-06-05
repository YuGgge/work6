package com.zhang.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhang
 * @date  2024/2/7
 * @Description 实现关注操作存储信息
 */
@TableName("follow")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Follow implements Serializable {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField("user_id")
    private String userId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField("follow_user_id")
    private String followId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField( "created_at")
    private Date createdAt;
    @JsonIgnore
    private int deleted;
}
