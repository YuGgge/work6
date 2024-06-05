package com.zhang.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhang
 * @date  2024/2/7
 * @Description 实现评论
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment implements Serializable {
    @Id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField( "user_id")
    private long userId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField( "video_id")
    private String videoId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField( "parent_id")
    private String parentId;
    @TableField( "like_count")
    private int likeCount;
    @TableField( "child_count")
    private int childCount;
    private String content;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField( "created_at")
    private Date createdAt;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField( "updated_at")
    private Date updatedAt;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField("deleted_at")
    private Date deletedAt;
    @JsonIgnore
    private int deleted;
}
