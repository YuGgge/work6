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
 * @Description 用户的喜欢列表
 */
@TableName("user_like")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLike implements Serializable {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField("user_id")
    private String userId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField("video_id")
    private String videoId;
    @TableField("comment_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String commentId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField( "created_at")
    private Date createdAt;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField("deleted_at")
    private Date deletedAt;
    @JsonIgnore
    private int deleted;
}
